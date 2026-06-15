package com.andozy.security_api.dto;

public record AuthResponse(
        String tokenType,
        String accessToken
) {
}
