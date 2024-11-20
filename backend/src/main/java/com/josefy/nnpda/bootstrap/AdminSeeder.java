package com.josefy.nnpda.bootstrap;

import com.josefy.nnpda.infrastructure.repository.IRoleRepository;
import com.josefy.nnpda.infrastructure.repository.IUserRepository;
import com.josefy.nnpda.infrastructure.utils.IHashProvider;
import com.josefy.nnpda.model.Role;
import com.josefy.nnpda.model.RoleEnum;
import com.josefy.nnpda.model.User;
import com.josefy.nnpda.service.IUserDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Order(2)
@Profile("!test")
public class AdminSeeder implements CommandLineRunner {
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final IHashProvider hashProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN).get();
        var admin = new User("admin", "vladimir.josefy@student.upce.cz", passwordEncoder.encode("admin"),
                adminRole);
        if (!userRepository.existsByUsername(admin.getUsername())) {
                userRepository.save(admin);
        }
    }
}
