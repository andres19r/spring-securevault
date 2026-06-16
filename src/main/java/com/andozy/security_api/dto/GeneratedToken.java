package com.andozy.security_api.dto;

import com.andozy.security_api.entity.RefreshToken;

public record GeneratedToken(String rawToken, RefreshToken refreshToken) {
}
