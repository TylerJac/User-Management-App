package org.uma.uma.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    private Set<User> users;

}
