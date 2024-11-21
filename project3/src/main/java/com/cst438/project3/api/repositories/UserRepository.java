package com.cst438.project3.api.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cst438.project3.api.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}