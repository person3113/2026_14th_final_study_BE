package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.SignupRequest;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = {
        "jwt.secret-key=thisisasecretkeyfortestingjwtproviderwhichisverylongandsecureenough"
})
@ActiveProfiles("test")
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("신규 사용자 회원가입 성공 테스트")
    void signupSuccess() {
        // given
        SignupRequest request = SignupRequest.builder()
                .email("signup@example.com")
                .password("password123")
                .nickname("사자닉네임")
                .build();

        // when
        authService.signup(request);

        // then
        User savedUser = userRepository.findByEmail(request.getEmail()).orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getNickname()).isEqualTo(request.getNickname());
        assertThat(savedUser.getEmail()).isEqualTo(request.getEmail());
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 가입 시도 시 예외 발생 테스트")
    void signupDuplicateEmailFail() {
        // given
        SignupRequest request1 = SignupRequest.builder()
                .email("duplicate@example.com")
                .password("password123")
                .nickname("사자닉네임1")
                .build();
        authService.signup(request1);

        SignupRequest request2 = SignupRequest.builder()
                .email("duplicate@example.com")
                .password("password456")
                .nickname("사자닉네임2")
                .build();

        // when & then
        assertThatThrownBy(() -> authService.signup(request2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 가입된 이메일입니다.");
    }

    @Test
    @DisplayName("정상 회원 로그인 성공 시 Access/Refresh Token 반환 테스트")
    void loginSuccess() {
        // given
        SignupRequest signupRequest = SignupRequest.builder()
                .email("login@example.com")
                .password("password123")
                .nickname("로그인사자")
                .build();
        authService.signup(signupRequest);

        LoginRequest loginRequest = LoginRequest.builder()
                .email("login@example.com")
                .password("password123")
                .build();

        // when
        String[] tokens = authService.login(loginRequest);

        // then
        assertThat(tokens).hasSize(2);
        assertThat(tokens[0]).isNotEmpty(); // Access Token
        assertThat(tokens[1]).isNotEmpty(); // Refresh Token
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 시도 시 예외 발생 테스트")
    void loginIncorrectPasswordFail() {
        // given
        SignupRequest signupRequest = SignupRequest.builder()
                .email("wrongpwd@example.com")
                .password("password123")
                .nickname("비번틀린사자")
                .build();
        authService.signup(signupRequest);

        LoginRequest loginRequest = LoginRequest.builder()
                .email("wrongpwd@example.com")
                .password("wrongpassword")
                .build();

        // when & then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이메일 또는 비밀번호가 잘못되었습니다.");
    }
}
