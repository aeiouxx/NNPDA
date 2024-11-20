package com.josefy.nnpda.infrastructure.controller;

import com.josefy.nnpda.infrastructure.dto.*;
import com.josefy.nnpda.infrastructure.service.IUserService;
import com.josefy.nnpda.infrastructure.service.IAuthenticationService;
import com.josefy.nnpda.infrastructure.utils.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Auth", description = "Manages operations concerning authentication of users.")
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final IAuthenticationService authenticationService;
    @PostMapping("/register")
    @Operation(
                summary = "Register user",
                description = "Register user with provided credentials, return JWT token on successful registration.",
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                        description = "User credentials",
                        required = true,
                        content = @Content(schema = @Schema(implementation = RegisterRequest.class))),
                responses = {
                        @ApiResponse(
                                description = "User registered successfully.",
                                responseCode = "200",
                                content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
                        @ApiResponse(
                                description = "User registration failed because of invalid request format.",
                                responseCode = "400"),
                        @ApiResponse(
                                description = "User with provided username or email already exists.",
                                responseCode = "409")
                }
        )
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return authenticationService.register(request).fold(Status::toResponseEntity, ResponseEntity::ok);
    }
    @PostMapping("/login")
    @Operation(
            summary = "Authenticate user",
            description = "Validate user credentials and return a JWT token on successful authentication.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User credentials",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))),
            responses = {
                    @ApiResponse(
                            description = "User authenticated successfully.",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
                    @ApiResponse(
                            description = "User authentication failed because of invalid request format.",
                            responseCode = "400"),
                    @ApiResponse(
                            description = "User authentication failed.",
                            responseCode = "401")
            })
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
        return authenticationService.login(request).fold(Status::toResponseEntity, ResponseEntity::ok);
    }
}
