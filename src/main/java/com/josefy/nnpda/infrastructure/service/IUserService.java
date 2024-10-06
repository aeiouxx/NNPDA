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
}
