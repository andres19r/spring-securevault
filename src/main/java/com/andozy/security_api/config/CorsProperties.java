package com.andozy.security_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "application.cors")
@Validated
@Getter
@Setter
public class CorsProperties {
    @NotBlank
    private String frontendUrl;
}
