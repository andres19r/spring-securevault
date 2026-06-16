package com.andozy.security_api.dto;

import lombok.Builder;

@Builder
public record AuthResponse(
        String tokenType,
        String accessToken,
        String refreshToken
) {
}
