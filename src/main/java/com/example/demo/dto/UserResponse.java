package com.example.demo.dto;

import com.example.demo.domain.User;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class UserResponse {
    private final Long id;
    private final String email;
    private final String nickname;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}
