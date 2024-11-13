package com.josefy.nnpda.bootstrap;

import com.josefy.nnpda.infrastructure.repository.IRoleRepository;
import com.josefy.nnpda.model.Role;
import com.josefy.nnpda.model.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Order(1)
public class RoleSeeder implements CommandLineRunner {
    private final IRoleRepository roleRepository;

    private void createMissingRoles() {
        Arrays.stream(RoleEnum.values())
                .forEach(roleName -> {
                    if (!roleRepository.existsByName(roleName)) {
                        Role missingRole = new Role(roleName);
                        roleRepository.save(missingRole);
                    }
                });
    }

    @Override
    public void run(String... args) throws Exception {
        createMissingRoles();
    }
}
