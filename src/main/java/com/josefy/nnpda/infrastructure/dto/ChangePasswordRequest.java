package com.josefy.nnpda.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChangePasswordRequest (
    @Schema(description = "Old password of the user", example = "password1234")
    String oldPassword,
    @Schema(description = "New password for the user", example = "password1234")
    String newPassword
){}
