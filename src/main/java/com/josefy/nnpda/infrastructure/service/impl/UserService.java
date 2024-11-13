package com.josefy.nnpda.infrastructure.service.impl;

import com.josefy.nnpda.infrastructure.utils.Either;
import com.josefy.nnpda.infrastructure.repository.IPasswordResetTokenRepository;
import com.josefy.nnpda.infrastructure.repository.IUserRepository;
import com.josefy.nnpda.infrastructure.service.IEmailService;
import com.josefy.nnpda.infrastructure.service.IUserService;
import com.josefy.nnpda.infrastructure.utils.Hashing;
import com.josefy.nnpda.infrastructure.utils.Status;
import com.josefy.nnpda.model.PasswordResetToken;
import com.josefy.nnpda.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private static Status USER_NOT_FOUND
            = new Status("User not found.", HttpStatus.NOT_FOUND);

    @Override
    public Either<Status, User> getById(Long id) {
        return userRepository.findById(id)
                .map(Either::<Status, User>right)
                .orElse(Either.left(USER_NOT_FOUND));
    }

    @Override
    public Either<Status, User> getByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(Either::<Status, User>right)
                .orElse(Either.left(USER_NOT_FOUND));
    }

    @Override
    public Either<Status, User> getByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(Either::<Status, User>right)
                .orElse(Either.left(USER_NOT_FOUND));
    }

    @Override
    public Either<Status, Void> delete(User user) {
        userRepository.delete(user);
        return Either.right(null);
    }

    @Override
    public Either<Status, User> save(User user) {
        var username = user.getUsername();
        if (userRepository.existsByUsername(username)) {
            var message = new Status("User '%s' already exists.".formatted(username),
                    HttpStatus.CONFLICT);
            return Either.left(message);
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            var message = new Status("Email '%s' is already registered.".formatted(user.getEmail()),
                    HttpStatus.CONFLICT);
            return Either.left(message);
        }

        return Either.right(userRepository.save(user));
    }

    @Override
    @Transactional
    public Either<Status, Void> requestPasswordReset(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return Either.left(USER_NOT_FOUND);
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
    public Either<Status, Void> resetPassword(String token, String password) {
        var hash = Hashing.sha256(token);
        PasswordResetToken resetToken = passwordResetTokenRepository.findByTokenHash(hash).orElse(null);
        if (resetToken == null || resetToken.isExpired(new Date())) {
            return Either.left(
                    new Status("Invalid or expired token.", HttpStatus.UNAUTHORIZED));
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
    public Either<Status, Void> changePassword(String username, String oldPassword, String newPassword) {
        var user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return Either.left(USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return Either.left(new Status( "Incorrect old password.",
                    HttpStatus.BAD_REQUEST));
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
