package org.uma.uma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uma.uma.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
