package org.uma.uma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uma.uma.entity.Role;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}

