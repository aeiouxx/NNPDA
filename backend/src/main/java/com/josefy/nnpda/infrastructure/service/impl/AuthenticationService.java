package com.josefy.nnpda.infrastructure.service.impl;

import com.josefy.nnpda.infrastructure.dto.AuthenticationResponse;
import com.josefy.nnpda.infrastructure.dto.LoginRequest;
import com.josefy.nnpda.infrastructure.dto.RegisterRequest;
import com.josefy.nnpda.infrastructure.exceptions.NotFoundException;
import com.josefy.nnpda.infrastructure.repository.IRoleRepository;
import com.josefy.nnpda.infrastructure.repository.IUserRepository;
import com.josefy.nnpda.infrastructure.security.JwtTokenProvider;
import com.josefy.nnpda.infrastructure.service.IAuthenticationService;
import com.josefy.nnpda.infrastructure.utils.Either;
import com.josefy.nnpda.infrastructure.utils.Status;
import com.josefy.nnpda.model.Role;
import com.josefy.nnpda.model.RoleEnum;
import com.josefy.nnpda.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Either<Status, AuthenticationResponse> login(LoginRequest request) {
        var user = userRepository.findByUsername(request.username()).orElse(null);
        if (user == null) {
            log.info("User with username '{}' not found", request.username());
            var message = "Invalid username or password.";
            return Either.left(new Status(message, HttpStatus.UNAUTHORIZED));
        }
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.info("Incorrect password for user '{}'", request.username());
            var message = "Invalid username or password.";
            return Either.left(new Status(message, HttpStatus.UNAUTHORIZED));
        }
        var response = createAuthenticationResponse(user);
        return Either.right(response);
    }

    @Override
    public Either<Status, AuthenticationResponse> register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            var message = "User with provided username already exists.";
            return Either.left(new Status(message, HttpStatus.CONFLICT));
        }
        if (userRepository.existsByEmail(request.email())) {
            var message = "User with provided email already exists";
            return Either.left(new Status(message, HttpStatus.CONFLICT));
        }
        var role = roleRepository.findByName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new NotFoundException(Role.class));
        var encodedPassword = passwordEncoder.encode(request.password());
        var newUser = userRepository.save(new User(request.username(), request.email(), encodedPassword, role));
        var response = createAuthenticationResponse(newUser);
        return Either.right(response);
    }

    @Override
    public Either<Status, AuthenticationResponse> validateToken(String token) {
        final String tokenUser = jwtTokenProvider.extractUsername(token);
        log.info("Validating token for user '{}'", tokenUser);
        final User matchingUser = userRepository.findByUsername(tokenUser).orElse(null);
        log.info("Matching user: {}", matchingUser);
        if (!jwtTokenProvider.isTokenValid(token, matchingUser)) {
            log.info("Token is invalid");
            var message = "Invalid token.";
            return Either.left(new Status(message, HttpStatus.UNAUTHORIZED));
        }
        var response = createAuthenticationResponse((User) matchingUser);
        log.info("Token is valid");
        return Either.right(response);
    }

    private AuthenticationResponse createAuthenticationResponse(User user) {
        var token = jwtTokenProvider.generateToken(user.getUsername(), user.getAuthorities());
        var authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
        var response = new AuthenticationResponse(
                token,
                user.getUsername(),
                authorities
        );
        return response;
    }
}
