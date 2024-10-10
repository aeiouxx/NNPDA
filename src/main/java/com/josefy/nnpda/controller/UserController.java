package com.josefy.nnpda.controller;

import com.josefy.nnpda.infrastructure.dto.ChangePasswordRequest;
import com.josefy.nnpda.infrastructure.dto.NewPasswordRequest;
import com.josefy.nnpda.infrastructure.dto.ResetPasswordRequest;
import com.josefy.nnpda.infrastructure.service.IAuthenticationService;
import com.josefy.nnpda.infrastructure.service.IUserService;
import com.josefy.nnpda.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "Manages operations concerning user accounts.")
@Controller
@RequestMapping("user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final IUserService userService;
    @Operation(
            summary = "Reset password for user",
            description = "Reset password for user with provided username.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Unique username",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ResetPasswordRequest.class))),
            responses = {
                    @ApiResponse(
                            description = "Password reset processed.",
                            responseCode = "200"),
                    @ApiResponse(
                            description = "Password reset failed because of invalid request format.",
                            responseCode = "400"),
                    // We don't respond with 404 on purpose,
                    // so that our hypothetical attacker doesn't know if the user exists or not
                    // (would make more sense if we accepted email instead of username)
            }
    )
    @PostMapping("/password-reset-request")
    public ResponseEntity<?> requestPasswordReset(@RequestBody @Valid ResetPasswordRequest request) {
        userService.requestPasswordReset(request.username());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Change password via token",
            description = "Change password for user with provided token.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New password",
                    required = true,
                    content = @Content(schema = @Schema(implementation = NewPasswordRequest.class))),
            responses = {
                    @ApiResponse(
                            description = "Password changed.",
                            responseCode = "200"),
                    @ApiResponse(
                            description = "Password change failed because of invalid request format.",
                            responseCode = "400"),
                    @ApiResponse(
                            description = "Password change failed because of invalid token (non-existent or expired).",
                            responseCode = "401"),
            }
    )
    @PutMapping("/change-password-token")
    public ResponseEntity<?> changePasswordViaToken(@RequestBody @Valid NewPasswordRequest request) {
        userService.resetPassword(request.token(), request.password());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Change password for current user",
            description = "Change password for currently authenticated user.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New password",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ChangePasswordRequest.class))),
            responses = {
                    @ApiResponse(
                            description = "Password changed.",
                            responseCode = "200"),
                    @ApiResponse(
                            description = "Password change failed because of invalid request format.",
                            responseCode = "400"),
                    @ApiResponse(
                            description = "Password change failed because of incorrect old password.",
                            responseCode = "401"),
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/me/change-password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal User user) {
        userService.changePassword(user.getUsername(), request.oldPassword(), request.newPassword());
        return ResponseEntity.ok().build();
    }
}
