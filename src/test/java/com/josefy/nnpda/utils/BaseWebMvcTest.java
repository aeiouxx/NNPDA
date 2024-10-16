package com.josefy.nnpda.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.josefy.nnpda.config.SecurityConfig;
import com.josefy.nnpda.infrastructure.security.JwtTokenProvider;
import com.josefy.nnpda.infrastructure.service.IUserService;
import com.josefy.nnpda.infrastructure.service.impl.EmailService;
import com.josefy.nnpda.infrastructure.utils.Either;
import com.josefy.nnpda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

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
    protected EmailService emailService;
    @MockBean
    protected JavaMailSender javaMailSender;
    @MockBean
    protected JwtTokenProvider jwtTokenProvider;
    @MockBean
    protected UserDetailsService userDetailsService;


    protected final User setMockUser(String username, String email, String password) {
        var user = new User(username, email, password);
        when(userService.getByUsername(username))
                .thenReturn(Either.right(user));
        return user;
    }
}
