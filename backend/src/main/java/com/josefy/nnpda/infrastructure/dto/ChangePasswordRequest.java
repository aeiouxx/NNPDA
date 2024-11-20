package com.josefy.nnpda.infrastructure.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;

public record ChangePasswordRequest (
    @Schema(description = "Old password of the user", example = "Password.12345678")
    @Length(min = 8, message = "Password must be at least 8 characters long.")
    String oldPassword,
    @Schema(description = "New password for the user", example = "Password.12345678!!")
    String newPassword
){}
