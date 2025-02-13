package com.devcambo.springinit.security;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDto(
        @NotEmpty(message = "Email is required!")
        String email,
        @NotEmpty(message = "Password is required!")
        String password
) {
}
