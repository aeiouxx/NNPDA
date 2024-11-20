package com.josefy.nnpda.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest (
        @Schema(description = "Username", example = "admin")
        @NotBlank(message = "Username is required.")
        String username,
        @Schema(description = "Password", example = "password1234")
        @NotBlank(message = "Password is required.")
        String password)
{}
