package com.josefy.nnpda.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest (@Schema(description = "Username", example = "admin") String username,
                            @Schema(description = "Password", example = "password1234") String password)
{}
