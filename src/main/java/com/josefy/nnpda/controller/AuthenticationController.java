package com.josefy.nnpda.controller;

import com.josefy.nnpda.infrastructure.dto.AuthResponse;
import com.josefy.nnpda.infrastructure.dto.LoginRequest;
import com.josefy.nnpda.infrastructure.security.JwtTokenProvider;
import com.josefy.nnpda.infrastructure.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final IUserService userService;
    private final JwtTokenProvider jwtTokenProvider;
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
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(
                            description = "User authentication failed.",
                            responseCode = "401")
            })
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest request){
        log.info("Authenticating user: {}", request.username());
        return userService.getUserByUsername(request.username())
                .map(user -> {
                    var token = jwtTokenProvider.generateToken(user.getUsername());
                    return ResponseEntity.ok(new AuthResponse(token));
                })
                .orElse(ResponseEntity.status(401).build());
    }
}
