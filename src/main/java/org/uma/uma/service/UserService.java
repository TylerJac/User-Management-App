package org.uma.uma.service;

import jakarta.annotation.PostConstruct;
import org.springframework.security.access.annotation.Secured;
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

@Service  // Marks this class as a Spring service, meaning it holds business logic and is managed by the Spring container
public class UserService {

    @Autowired
    private UserRepository userRepository;  // Injects the UserRepository for performing CRUD operations on User entities

    @Autowired
    private RoleRepository roleRepository;  // Injects the RoleRepository for CRUD operations on Role entities

    @Autowired
    private PasswordEncoder passwordEncoder;  // Injects the PasswordEncoder for hashing passwords

    // Initializes the roles in the system after the service is created (runs once after initialization)
    @PostConstruct
    public void initRoles() {
        Set<String> roleNames = new HashSet<>();  // Set of role names to initialize
        roleNames.add("ADMIN");  // Add ADMIN role
        roleNames.add("USER");   // Add USER role

        // Loop through the role names and check if each role exists in the database
        for (String roleName : roleNames) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                // If the role is not found in the database, create and save it
                roleRepository.save(new Role(roleName));
            }
        }
    }

    // Saves the user with the assigned roles, and hashes the password before saving
    public void saveUser(User user, Set<Role> roles) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Hash the password before saving
        user.setRoles(roles);  // Assign the provided roles to the user
        userRepository.save(user);  // Save the user entity in the database
    }

    // Finds a user by username; throws IllegalArgumentException if the user is not found
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));  // If not found, throw an exception
    }

    // Fetches all users from the database
    @Secured({"ADMIN"})  // Only admins can access this method
    public List<User> getAllUsers() {
        return userRepository.findAll();  // Return all users as a list
    }

    // Returns all roles in the system as a Set
    public Set<Role> getRoles() {
        return new HashSet<>(roleRepository.findAll());  // Return all roles from the RoleRepository
    }

    // Deletes a user by their ID (for admin operations)
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);  // Delete the user with the given ID from the database
    }

    // Fetches a user by their ID; throws IllegalArgumentException if the user is not found (for admin operations)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));  // If not found, throw an exception
    }
}
