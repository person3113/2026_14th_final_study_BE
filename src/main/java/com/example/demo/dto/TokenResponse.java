package com.example.demo.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class TokenResponse {
    private final String accessToken;
}
