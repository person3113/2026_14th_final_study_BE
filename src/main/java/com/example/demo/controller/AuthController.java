package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.SignupRequest;
import com.example.demo.dto.TokenResponse;
import com.example.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "회원가입, 로그인 및 토큰 제어를 수행하는 인증 API")
public class AuthController {

    private final AuthService authService;

    /**
     * 회원 가입 API 엔드포인트
     */
    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "새로운 회원을 서비스에 등록합니다. 이메일 형식과 비밀번호 길이(최소 4자) 검증이 수행됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 오류 또는 이미 가입된 이메일")
    })
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok("회원가입에 성공했습니다.");
    }

    /**
     * 로그인 API 엔드포인트 (Access Token 바디 반환 & Refresh Token HttpOnly 쿠키 적재)
     */
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "인증에 성공하면 Access Token은 Body로, Refresh Token은 HttpOnly/Secure 보안 쿠키로 전달합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "로그인 자격 증명 실패")
    })
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
    @Operation(summary = "Access Token 재발급", description = "클라이언트 브라우저의 HttpOnly 쿠키에 실려있는 Refresh Token을 대조/검증하여 새로운 Access Token을 재발행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @ApiResponse(responseCode = "400", description = "만료되었거나 부적절한 리프레시 토큰")
    })
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
