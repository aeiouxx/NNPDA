package com.josefy.nnpda.controller;

import com.josefy.nnpda.config.SecurityConfig;
import com.josefy.nnpda.infrastructure.dto.LoginRequest;
import com.josefy.nnpda.infrastructure.security.JwtTokenProvider;
import com.josefy.nnpda.infrastructure.service.IUserService;
import com.josefy.nnpda.infrastructure.service.impl.UserService;
import com.josefy.nnpda.model.User;
import com.josefy.nnpda.utils.BaseWebMvcTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest extends BaseWebMvcTest {
    @Test
    public void shouldAuthenticateUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test_user", "test_password");
        var email = "test_email@test.io";
        var token = "FAKE_TOKEN_RESPONSE";
        var mockUser = setMockUser(loginRequest.username(), email, loginRequest.password());
        when(jwtTokenProvider.generateToken(mockUser.getUsername())) .thenReturn(token);
        mockMvc.perform(
                post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\":\"test_user\",\"password\":\"test_password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }
}
