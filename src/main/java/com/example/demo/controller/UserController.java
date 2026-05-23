package com.example.demo.controller;

import com.example.demo.dto.UserResponse;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "인증된 사용자의 정보 프로필 조회를 수행하는 회원 API")
public class UserController {

    private final UserService userService;

    /**
     * 내 정보(마이페이지) 조회 API 엔드포인트 (토큰 검증 후 접근 가능)
     */
    @GetMapping("/me")
    @Operation(
            summary = "내 정보 조회 (마이페이지)", 
            description = "Header의 Authorization Bearer JWT Access Token을 검증하여 로그인된 사용자의 이메일 및 닉네임 정보를 조회합니다.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 또는 만료된 토큰"),
            @ApiResponse(responseCode = "403", description = "인가 실패 (접근 권한 부족)")
    })
    public ResponseEntity<UserResponse> getMyProfile(@AuthenticationPrincipal String email) {
        log.info("Request get my profile - Authenticated user email: {}", email);
        UserResponse response = userService.getUserProfile(email);
        return ResponseEntity.ok(response);
    }
}
