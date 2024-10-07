package com.josefy.nnpda.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.josefy.nnpda.config.SecurityConfig;
import com.josefy.nnpda.controller.AuthenticationController;
import com.josefy.nnpda.infrastructure.security.JwtTokenProvider;
import com.josefy.nnpda.infrastructure.service.IUserService;
import com.josefy.nnpda.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest
@Import(SecurityConfig.class)
public  class BaseWebMvcTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected IUserService userService;
    @MockBean
    protected JwtTokenProvider jwtTokenProvider;
    @MockBean
    protected UserDetailsService userDetailsService;


    protected final User setMockUser(String username, String email, String password) {
        var user = new User(username, email, password);
        when(userService.getUserByUsername(username))
                .thenReturn(Optional.of(user));
        return user;
    }
}
