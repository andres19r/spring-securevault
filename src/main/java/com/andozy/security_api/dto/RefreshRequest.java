package com.andozy.security_api.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(@NotBlank(message = "Refresh token cannot be blank") String refreshToken) {
}
