package com.josefy.nnpda.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthenticationResponse(
        @Schema(description = "JWT token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,
        @Schema(description = "Username", example = "admin")
        String username,
        @Schema(description = "User role", example = "ADMIN")
        String[] roles
)
{
}
