package com.andozy.security_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email(message = "email must be valid")
        @NotBlank(message = "email cannot be blank")
        String email,

        @NotBlank(message = "password cannot be blank")
        @Size(min = 8, message = "password must have at least 8 characters")
        String password
) {
}
