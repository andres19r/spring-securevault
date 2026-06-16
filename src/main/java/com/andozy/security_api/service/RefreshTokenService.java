package com.andozy.security_api.service;

import com.andozy.security_api.auth.JwtService;
import com.andozy.security_api.config.SecurityProperties;
import com.andozy.security_api.dto.AuthResponse;
import com.andozy.security_api.dto.GeneratedToken;
import com.andozy.security_api.entity.RefreshToken;
import com.andozy.security_api.entity.User;
import com.andozy.security_api.exception.InvalidRefreshTokenException;
import com.andozy.security_api.repository.RefreshTokenRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
    private final RefreshTokenRepository repository;
    private final SecurityProperties securityProperties;
    private final JwtService jwtService;

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public GeneratedToken createForLogin(User user) {
        return create(user, null);
    }

    public GeneratedToken createForRefresh(User user, UUID familyId) {
        return create(user, familyId);
    }

    @Transactional
    private GeneratedToken create(User user, UUID familyId) {
        var rawToken = generateToken();
        var refreshToken = RefreshToken.builder()
                .tokenHash(sha256(rawToken))
                .familyId((familyId != null) ? familyId : UUID.randomUUID())
                .expiresAt(Instant.now().plusMillis(securityProperties.getRefreshTokenExpiration()))
                .user(user)
                .revoked(false)
                .build();
        repository.save(refreshToken);
        return new GeneratedToken(rawToken, refreshToken);
    }

    @Transactional
    public AuthResponse refresh(String rawRefreshToken) {
        RefreshToken token = repository.findByTokenHash(sha256(rawRefreshToken))
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid token"));
        if (token.isRevoked()) {
            repository.revokeFamily(token.getFamilyId());
            log.warn("Refresh token reuse detected for family {}", token.getFamilyId());
            throw new InvalidRefreshTokenException("Invalid token");
        }
        var tokenExpiration = token.getExpiresAt();
        if (tokenExpiration.isBefore(Instant.now())) {
            throw new InvalidRefreshTokenException("Invalid token");
        }

        token.setRevoked(true);
        repository.save(token);
        GeneratedToken newToken = createForRefresh(token.getUser(), token.getFamilyId());
        String newAccessToken = jwtService.generateAccessToken(token.getUser());
        return new AuthResponse("Bearer", newAccessToken, newToken.rawToken());
    }
}
