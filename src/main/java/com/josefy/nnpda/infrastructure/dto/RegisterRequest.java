package com.josefy.nnpda.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public record RegisterRequest(
        @Schema(description = "Username", example = "admin")
        @NotBlank(message = "Username is required.")
        String username,
        @Schema(description = "Email", example = "admin@network.io")
        @NotBlank(message = "Email is required.")
        @Email(message = "Email must be valid")
        String email,
        @Schema(description = "Password", example = "Password.12345678")
        @NotBlank(message = "Password is required.")
        @Length(min = 8, message = "Password must be at least 8 characters long.")
        String password
) {
}
