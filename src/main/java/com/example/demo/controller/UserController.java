package com.example.demo.controller;

import com.example.demo.dto.UserResponse;
import com.example.demo.service.UserService;
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
public class UserController {

    private final UserService userService;

    /**
     * 내 정보(마이페이지) 조회 API 엔드포인트 (토큰 검증 후 접근 가능)
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(@AuthenticationPrincipal String email) {
        log.info("Request get my profile - Authenticated user email: {}", email);
        UserResponse response = userService.getUserProfile(email);
        return ResponseEntity.ok(response);
    }
}
