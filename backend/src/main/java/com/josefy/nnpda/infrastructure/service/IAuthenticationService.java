package com.josefy.nnpda.infrastructure.service;

import com.josefy.nnpda.infrastructure.dto.AuthenticationResponse;
import com.josefy.nnpda.infrastructure.dto.LoginRequest;
import com.josefy.nnpda.infrastructure.dto.RegisterRequest;
import com.josefy.nnpda.infrastructure.dto.ResetPasswordRequest;
import com.josefy.nnpda.infrastructure.utils.Either;
import com.josefy.nnpda.infrastructure.utils.Status;

public interface IAuthenticationService {
    public Either<Status, AuthenticationResponse> login(LoginRequest request);
    public Either<Status, AuthenticationResponse> register(RegisterRequest request);
}
