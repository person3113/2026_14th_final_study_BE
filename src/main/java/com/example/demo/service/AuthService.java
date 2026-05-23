package com.example.demo.service;

import com.example.demo.domain.RefreshToken;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.SignupRequest;
import com.example.demo.dto.TokenResponse;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    /**
     * 회원 가입 비즈니스 로직
     */
    @Transactional
    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Signup failure: email already exists - {}", request.getEmail());
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .role(Role.ROLE_USER) // 기본 권한 USER 부여
                .build();

        userRepository.save(user);
        log.info("Successfully registered new user: {}", request.getEmail());
    }

    /**
     * 로그인 비즈니스 로직 (Access / Refresh Token 동시 발급)
     */
    @Transactional
    public String[] login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        String accessToken = jwtProvider.createAccessToken(user.getEmail(), user.getRole().name());
        String refreshTokenString = jwtProvider.createRefreshToken(user.getEmail());

        // Refresh Token DB 저장 및 갱신 (1인 1토큰 보장)
        LocalDateTime expiryDate = LocalDateTime.now().plusNanos(jwtProvider.getRefreshTokenValidityInMilliseconds() * 1_000_000L);
        
        refreshTokenRepository.findByEmail(user.getEmail())
                .ifPresentOrElse(
                        existingToken -> {
                            existingToken.updateToken(refreshTokenString, expiryDate);
                            log.info("Updated existing Refresh Token for user: {}", user.getEmail());
                        },
                        () -> {
                            RefreshToken newToken = RefreshToken.builder()
                                    .email(user.getEmail())
                                    .token(refreshTokenString)
                                    .expiryDate(expiryDate)
                                    .build();
                            refreshTokenRepository.save(newToken);
                            log.info("Saved new Refresh Token for user: {}", user.getEmail());
                        }
                );

        return new String[]{accessToken, refreshTokenString};
    }

    /**
     * Refresh Token을 활용한 Access Token 재발급 로직
     */
    @Transactional
    public TokenResponse reissue(String refreshTokenValue) {
        if (refreshTokenValue == null || !jwtProvider.validateToken(refreshTokenValue)) {
            log.warn("Token reissue failure: Invalid or blank Refresh Token");
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        String email = jwtProvider.getEmail(refreshTokenValue);

        RefreshToken savedToken = refreshTokenRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않았거나 파기된 리프레시 토큰입니다."));

        // 토큰 값 비교 및 만료 여부 확인
        if (!savedToken.getToken().equals(refreshTokenValue) || savedToken.isExpired(LocalDateTime.now())) {
            // 위변조 또는 만료 상황 시 해당 토큰 파기 처리
            refreshTokenRepository.delete(savedToken);
            log.warn("Token mismatch or expired. Deleted token for user: {}", email);
            throw new IllegalArgumentException("만료되거나 유효하지 않은 리프레시 토큰입니다. 다시 로그인해 주세요.");
        }

        // 새로운 Access Token 발급 (기존 Refresh Token은 유지)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String newAccessToken = jwtProvider.createAccessToken(user.getEmail(), user.getRole().name());
        log.info("Reissued new Access Token for user: {}", email);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }
}
