package com.andozy.security_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "application.security.jwt")
@Validated
@Data
public class SecurityProperties {
    @NotBlank
    private String secretKey;
    @Positive
    private long accessTokenExpiration;
    @Positive
    private long refreshTokenExpiration;
}
