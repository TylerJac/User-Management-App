package org.uma.uma.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.uma.uma.validation.CustomValidation;

import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @CustomValidation(valueType = CustomValidation.Type.USERNAME, message = "Username must be 5-20 characters long and contain only letters, numbers, and underscores")
    @Column(nullable = false, unique = true)
    private String username;

    @NotNull
    @CustomValidation(valueType = CustomValidation.Type.PASSWORD, message = "Password must be at least 8 characters long, with at least one uppercase letter and one number") // Custom annotation to validate password strength
    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}

