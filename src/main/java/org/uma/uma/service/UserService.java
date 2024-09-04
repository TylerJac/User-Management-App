package org.uma.uma.service;

import jakarta.annotation.PostConstruct;
import org.uma.uma.entity.Role;
import org.uma.uma.entity.User;
import org.uma.uma.repository.RoleRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.uma.uma.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostConstruct
    public void initRoles() {
        Set<String> roleNames = new HashSet<>();
        roleNames.add("ADMIN");
        roleNames.add("STAFF");

        for (String roleName : roleNames) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                roleRepository.save(new Role(roleName));
            }
        }
    }
    public void saveUser(User user,  Set<Role> roles) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
               .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Set<Role> getRoles() {
        return new HashSet<>(roleRepository.findAll());
    }

}

