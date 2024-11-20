package com.josefy.nnpda.infrastructure.service;

import org.springframework.context.annotation.Bean;

public interface IEmailService {
    public void sendPasswordReset(String email, String token);
}