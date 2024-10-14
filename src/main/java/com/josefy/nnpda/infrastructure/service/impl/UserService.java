package com.josefy.nnpda.infrastructure.service.impl;

import com.josefy.nnpda.infrastructure.Either;
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
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final IPasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;

    @Override
    public Either<String, User> getById(Long id) {
        return userRepository.findById(id)
                .map(Either::<String, User>right)
                .orElse(Either.left("User not found."));
    }

    @Override
    public Either<String, User> getByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(Either::<String, User>right)
                .orElse(Either.left("User not found."));
    }

    @Override
    public Either<String, User> getByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(Either::<String, User>right)
                .orElse(Either.left("User not found."));
    }

    @Override
    public Either<String, Void> delete(User user) {
        userRepository.delete(user);
        return Either.right(null);
    }

    @Override
    public Either<String, User> save(User user) {
        var username = user.getUsername();
        if (userRepository.existsByUsername(username)) {
            return Either.left("User '{}' already exists.".formatted(username));
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            return Either.left("Email '{}' is already registered.".formatted(user.getEmail()));
        }

        return Either.right(userRepository.save(user));
    }

    @Override
    @Transactional
    public Either<String, Void> requestPasswordReset(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return Either.left("User not found.");
        }
        var currentTime = new Date();
        var existingToken = passwordResetTokenRepository.findByUserUsername(username);
        if (existingToken.isPresent() && !existingToken.get().isExpired(currentTime)) {
             // Do nothing if token is still valid (could do other things also)
            return Either.right(null);
        }
        var data = generateNextTokenData(currentTime);
        var token = new PasswordResetToken(data.hash, data.expiration, user);
        log.info("Generated token '{}' for user '{}', email '{}'.", data.hash, user.getUsername(), user.getEmail());
        passwordResetTokenRepository.save(token);
        emailService.sendPasswordReset(user.getEmail(), data.text);
        return Either.right(null);
    }

    @Override
    @Transactional
    public Either<String, Void> resetPassword(String token, String password) {
        var hash = Hashing.sha256(token);
        PasswordResetToken resetToken = passwordResetTokenRepository.findByTokenHash(hash).orElse(null);
        if (resetToken == null || resetToken.isExpired(new Date())) {
            return Either.left("Invalid or expired token.");
        }

        var user = resetToken.getUser();
        var newPassword = passwordEncoder.encode(password);
        user.setPassword(newPassword);
        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);
        return Either.right(null);
    }
    @Override
    @Transactional
    public Either<String, Void> changePassword(String username, String oldPassword, String newPassword) {
        var user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return Either.left("User not found.");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return Either.left("Invalid password.");
        }
        var newHashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(newHashedPassword);
        userRepository.save(user);
        return Either.right(null);
    }
    private record TokenData(String text, String hash, Date expiration) {}
    private TokenData generateNextTokenData(Date currentTime) {
        var random = new SecureRandom();
        var bytes = new byte[64];
        random.nextBytes(bytes);
        var text = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        String hash = Hashing.sha256(text);
        var expiration = new Date(currentTime.getTime() + PasswordResetToken.EXPIRATION_MILLIS);
        return new TokenData(text, hash, expiration);
    }
}
