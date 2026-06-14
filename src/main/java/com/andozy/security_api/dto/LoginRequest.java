package com.andozy.security_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email(message = "email must be valid")
        @NotBlank(message = "email cannot be blank")
        String email,

        @NotBlank(message = "password cannot be blank")
        String password
) {
}
