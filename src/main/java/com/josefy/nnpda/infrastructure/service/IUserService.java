package com.josefy.nnpda.infrastructure.service;

import com.josefy.nnpda.infrastructure.Either;
import com.josefy.nnpda.model.User;
import jakarta.transaction.Transactional;
import org.springframework.security.config.core.userdetails.ReactiveUserDetailsServiceResourceFactoryBean;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface IUserService {
    public Either<String, User> getById(Long id);
    public Either<String, User> getByUsername(String username);
    public Either<String, User> getByEmail(String email);
    public Either<String, Void> delete(User user);
    public Either<String, User> save(User user);


    public Either<String, Void> requestPasswordReset(String username);
    public Either<String, Void> resetPassword(String token, String password);
    public Either<String, Void> changePassword(String username,
                                               String oldPassword,
                                               String newPassword);
}
