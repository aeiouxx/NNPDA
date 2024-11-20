package com.josefy.nnpda.infrastructure.repository;

import com.josefy.nnpda.model.Role;
import com.josefy.nnpda.model.RoleEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IRoleRepository extends CrudRepository<Role, Long> {
    boolean existsByName(RoleEnum name);

    Optional<Role> findByName(RoleEnum name);
}
