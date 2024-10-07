package com.josefy.nnpda.infrastructure.service;

import com.josefy.nnpda.infrastructure.dto.AuthenticationResponse;
import com.josefy.nnpda.infrastructure.dto.LoginRequest;
import com.josefy.nnpda.infrastructure.dto.RegisterRequest;

public interface IAuthenticationService {
    public AuthenticationResponse login(LoginRequest request);
    public AuthenticationResponse register(RegisterRequest request);
    public AuthenticationResponse refresh(String request);
}
