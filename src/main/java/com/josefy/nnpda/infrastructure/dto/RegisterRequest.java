package com.josefy.nnpda.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @Schema(description = "Username", example = "admin")
        @NotBlank(message = "Username is required.")
        String username,
        @Schema(description = "Email", example = "admin@network.io")
        @NotBlank(message = "Email is required.")
        @Email(message = "Email must be valid")
        String email,
        @Schema(description = "Password", example = "password1234")
        @NotBlank(message = "Password is required.")
        String password
) {
}
