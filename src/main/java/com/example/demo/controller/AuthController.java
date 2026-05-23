package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.SignupRequest;
import com.example.demo.dto.TokenResponse;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 회원 가입 API 엔드포인트
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok("회원가입에 성공했습니다.");
    }

    /**
     * 로그인 API 엔드포인트 (Access Token 바디 반환 & Refresh Token HttpOnly 쿠키 적재)
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        String[] tokens = authService.login(request);
        String accessToken = tokens[0];
        String refreshToken = tokens[1];

        // 실무 표준에 입각한 HttpOnly, Secure 보안 쿠키 빌드
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)            // 자바스크립트 XSS 탈취 차단
                .secure(true)              // HTTPS 통신 프로토콜 강제 적용
                .path("/")                 // 호스트 전체 도메인 전송 범위 지정
                .maxAge(7 * 24 * 60 * 60)  // 7일간 쿠키 유효수명 지정
                .sameSite("None")          // 크로스 오리진(CORS) 프론트엔드 연동 지원
                .build();

        log.info("Successfully completed login for user: {}. Set HttpOnly Cookie.", request.getEmail());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(TokenResponse.builder().accessToken(accessToken).build());
    }

    /**
     * Refresh Token을 쿠키에서 가공하여 신규 Access Token 재발급 엔드포인트
     */
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(
            @CookieValue(value = "refreshToken", required = false) String refreshToken) {
        
        if (refreshToken == null) {
            log.warn("Token reissue failure: Refresh Token cookie is empty.");
            throw new IllegalArgumentException("리프레시 토큰이 존재하지 않습니다. 다시 로그인해 주세요.");
        }

        TokenResponse tokenResponse = authService.reissue(refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }
}
