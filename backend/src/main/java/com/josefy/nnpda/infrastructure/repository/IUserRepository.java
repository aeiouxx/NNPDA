package com.josefy.nnpda.infrastructure.repository;

import com.josefy.nnpda.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    List<User> findAll();

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsernameOrEmail(String username, String email);
    void deleteByUsername(String username);
    void deleteByEmail(String email);
}
