package com.josefy.nnpda.infrastructure.service.impl;

import com.josefy.nnpda.infrastructure.dto.AuthenticationResponse;
import com.josefy.nnpda.infrastructure.dto.LoginRequest;
import com.josefy.nnpda.infrastructure.dto.RegisterRequest;
import com.josefy.nnpda.infrastructure.exceptions.UnauthorizedException;
import com.josefy.nnpda.infrastructure.repository.IUserRepository;
import com.josefy.nnpda.infrastructure.security.JwtTokenProvider;
import com.josefy.nnpda.infrastructure.service.IAuthenticationService;
import com.josefy.nnpda.infrastructure.service.IUserService;
import com.josefy.nnpda.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private final IUserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    @Override
    public AuthenticationResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password."));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid username or password.");
        }
        var token = jwtTokenProvider.generateToken(user.getUsername());
        return new AuthenticationResponse(token);
    }

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UnauthorizedException("User with provided username already exists.");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new UnauthorizedException("User with provided email already exists.");
        }
        var encodedPassword = passwordEncoder.encode(request.password());
        var newUser = userRepository.save(new User(request.username(), request.email(), encodedPassword));
        var token = jwtTokenProvider.generateToken(newUser.getUsername());
        return new AuthenticationResponse(token);
    }

    @Override
    public AuthenticationResponse refresh(String request) {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
