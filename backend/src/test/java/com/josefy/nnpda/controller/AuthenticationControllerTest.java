package com.josefy.nnpda.controller;

import com.josefy.nnpda.infrastructure.controller.AuthenticationController;
import com.josefy.nnpda.infrastructure.dto.AuthenticationResponse;
import com.josefy.nnpda.infrastructure.dto.LoginRequest;
import com.josefy.nnpda.infrastructure.dto.RegisterRequest;
import com.josefy.nnpda.infrastructure.exceptions.ConflictException;
import com.josefy.nnpda.infrastructure.exceptions.UnauthorizedException;
import com.josefy.nnpda.infrastructure.service.IAuthenticationService;
import com.josefy.nnpda.infrastructure.utils.Either;
import com.josefy.nnpda.utils.BaseWebMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest extends BaseWebMvcTest {
    @MockBean
    private IAuthenticationService authenticationService;

    @Test
    public void shouldLoginUser() throws Exception {
        var request = new LoginRequest("test_user", "test_password");
        var token = "FAKE_TOKEN_RESPONSE";
        var authResponse = new AuthenticationResponse(token);
        when(authenticationService.login(any(LoginRequest.class))).thenReturn(
                Either.right(authResponse));
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    public void shouldRejectBadRequestLogin() throws Exception {
        var invalidRequestContent = "{\"username\":\"\"}"; // Missing password field or invalid JSON structure
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectInvalidCredentials() throws Exception {
        var request = new LoginRequest("test_user", "wrong_password");
        when(authenticationService.login(any(LoginRequest.class)))
                .thenThrow(new UnauthorizedException("Invalid username or password."));
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }


    @Test
    public void shouldRegisterUserSuccessfully() throws Exception {
        // Arrange
        var request = new RegisterRequest("new_user", "new_email@test.io", "password123");
        var token = "FAKE_TOKEN_RESPONSE";
        var authResponse = new AuthenticationResponse(token);

        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(
                Either.right(authResponse));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    public void shouldRejectBadRequestRegister() throws Exception {
        var invalidRequestContent = "{\"username\":\"\"}";
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectRegistrationWhenUserOrEmailCollides() throws Exception {
        var request = new RegisterRequest("existing_user", "existing_email@test.io", "password123");
        when(authenticationService.register(any(RegisterRequest.class)))
                .thenThrow(new ConflictException("User with provided username or email already exists."));
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").value("User with provided username or email already exists."));
    }
}
