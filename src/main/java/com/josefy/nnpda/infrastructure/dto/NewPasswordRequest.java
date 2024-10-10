package com.josefy.nnpda.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

public record NewPasswordRequest(
        @Schema(description = "Generated token for the reset request", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,
        @Schema(description = "New password for the user", example = "password1234")
        @Min(value = 8, message = "Password must be at least 8 characters long")
        String password
){}
