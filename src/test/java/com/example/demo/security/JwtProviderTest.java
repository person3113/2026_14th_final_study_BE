package com.example.demo.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "jwt.secret-key=thisisasecretkeyfortestingjwtproviderwhichisverylongandsecureenough"
})
@ActiveProfiles("test")
class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("JWT Access Token 발급 및 클레임 파싱 성공 테스트")
    void createAndParseAccessTokenSuccess() {
        // given
        String email = "test@example.com";
        String role = "ROLE_USER";

        // when
        String token = jwtProvider.createAccessToken(email, role);
        
        // then
        assertThat(token).isNotNull();
        assertThat(jwtProvider.validateToken(token)).isTrue();
        assertThat(jwtProvider.getEmail(token)).isEqualTo(email);
        assertThat(jwtProvider.getRole(token)).isEqualTo(role);
    }

    @Test
    @DisplayName("JWT Refresh Token 발급 및 검증 성공 테스트")
    void createAndValidateRefreshTokenSuccess() {
        // given
        String email = "refresh@example.com";

        // when
        String token = jwtProvider.createRefreshToken(email);

        // then
        assertThat(token).isNotNull();
        assertThat(jwtProvider.validateToken(token)).isTrue();
        assertThat(jwtProvider.getEmail(token)).isEqualTo(email);
    }

    @Test
    @DisplayName("유효하지 않거나 깨진 토큰 검증 실패 테스트")
    void validateInvalidTokenFail() {
        // given
        String brokenToken = "eyJhbGciOiJIUzI1NiJ9.brokenPayload.brokenSignature";

        // when
        boolean isValid = jwtProvider.validateToken(brokenToken);

        // then
        assertThat(isValid).isFalse();
    }
}
