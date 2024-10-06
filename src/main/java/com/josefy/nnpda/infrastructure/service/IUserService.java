package com.josefy.nnpda.infrastructure.service;

import com.josefy.nnpda.model.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

public interface IUserService {
    public User getUserById(Long id);
    public User getUserByUsername(String username);
    public User getUserByEmail(String email);
    public void deleteUser(User user);
}
