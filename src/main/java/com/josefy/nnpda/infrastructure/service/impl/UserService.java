package com.josefy.nnpda.infrastructure.service.impl;

import com.josefy.nnpda.infrastructure.exceptions.*;
import com.josefy.nnpda.infrastructure.repository.IPasswordResetTokenRepository;
import com.josefy.nnpda.infrastructure.repository.IUserRepository;
import com.josefy.nnpda.infrastructure.service.IEmailService;
import com.josefy.nnpda.infrastructure.service.IUserService;
import com.josefy.nnpda.infrastructure.utils.Hashing;
import com.josefy.nnpda.model.PasswordResetToken;
import com.josefy.nnpda.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final IPasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        userRepository.deleteByUsername(user.getUsername());
    }

    @Override
    @Transactional
    public User save(User user) {
        if (userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            throw new ConflictException("User with provided username or email already exists.");
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void requestPasswordReset(String username) {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            log.info("Attempted to reset password for non-existing user: {}", username);
            return;
        }

        var existingToken
                = passwordResetTokenRepository.findByUserUsername(username);
        if (existingToken.isPresent()) {
            var expiryDate = existingToken.get().getExpiryDate();
            if (expiryDate.after(Calendar.getInstance().getTime())) {
                return;
            }
            passwordResetTokenRepository.delete(existingToken.get());
        }

        var random = new SecureRandom();
        var bytes = new byte[64];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        String hash = Hashing.sha256(token);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);
        var expiryDate = calendar.getTime();

        var passwordResetToken = new PasswordResetToken(hash, expiryDate, user.get());
        passwordResetTokenRepository.save(passwordResetToken);
        var email = user.get().getEmail();
        emailService.sendPasswordReset(email, token);
    }


    @Override
    @Transactional
    public void resetPassword(String token, String password) {
        var hash = Hashing.sha256(token);
        var passwordResetToken = passwordResetTokenRepository.findByTokenHash(hash);
        if (passwordResetToken.isEmpty()) {
            log.info("Attempted to reset password with invalid token: {}", token);
            throw new InvalidTokenException("Invalid token.");
        }
        if (passwordResetToken.get().getExpiryDate().before(Calendar.getInstance().getTime())) {
            log.info("Attempted to reset password with expired token: {}", token);
            throw new UnauthorizedException("Expired token.");
        }
        var user = passwordResetToken.get().getUser();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        passwordResetTokenRepository.delete(passwordResetToken.get());
    }

    @Override
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new NotFoundException(User.class);
        }
        if (!passwordEncoder.matches(oldPassword, user.get().getPassword())) {
            throw new BadRequestException("Invalid old password.");
        }
        user.get().setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user.get());
    }
}
