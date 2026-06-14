package com.andozy.security_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String error,
        @JsonInclude(JsonInclude.Include.NON_NULL) String message,
        @JsonInclude(JsonInclude.Include.NON_NULL) Map<String, String> errors
) {
}
