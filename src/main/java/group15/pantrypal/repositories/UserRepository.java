package group15.pantrypal.repositories;

import group15.pantrypal.model.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<user, Long> {
    Optional<user> findByUsername(String username);
}