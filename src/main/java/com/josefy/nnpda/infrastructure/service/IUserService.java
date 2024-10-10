package com.josefy.nnpda.infrastructure.service;

import com.josefy.nnpda.model.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface IUserService {

    public Optional<User> getUserById(Long id);
    public Optional<User> getUserByUsername(String username);
    public Optional<User> getUserByEmail(String email);
    public void deleteUser(User user);
    public User save(User user);


    public void requestPasswordReset(String username);
    public void resetPassword(String token, String password);
    public void changePassword(String username, String oldPassword, String newPassword);
}
