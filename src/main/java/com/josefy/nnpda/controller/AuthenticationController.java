package com.josefy.nnpda.controller;

import com.josefy.nnpda.infrastructure.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Auth", description = "Manages operations concerning authentication of users.")
@Controller
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    record LoginRequest(String username, String password) {}
    record AuthResponse(String token) {}
    @PostMapping("/login")
    @Operation(
            summary = "Authenticate user",
            description = "Authenticate user with provided credentials.",
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
        log.info("Login request: " + request.password);
        return ResponseEntity.ok(new AuthResponse("token_boj"));
    }
}
