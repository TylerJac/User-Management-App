package org.uma.uma.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.uma.uma.entity.User;
import org.uma.uma.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/api")
@RestController  // Marks this class as a Spring MVC controller
public class UserController {

    @Autowired
    private UserService userService;  // Inject the UserService to handle user operations

    // View a list of all users (for admins)
    @GetMapping("/users/all")
    @PreAuthorize("hasRole('ADMIN')")  // Only admins can access this
    public ResponseEntity<List<User>> viewAllUsers() {
        List<User> users = userService.getAllUsers();  // Fetch all users
        return ResponseEntity.ok(users);  // Return the list of users as JSON
    }

    // Display details of the logged-in user
    @GetMapping("/user/details")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")  // Allow both USER and ADMIN to access
    public ResponseEntity<User> showUserDetails(Principal principal) {
        String username = principal.getName();  // Get the username of the currently logged-in user
        User user = userService.findByUsername(username);  // Find the user by their username
        return ResponseEntity.ok(user);  // Return the user details as JSON
    }

    // Handle updating the logged-in user's details
    @PostMapping("/user/update")
    public ResponseEntity<?> updateUserDetails( @RequestBody User user, Principal principal) {

        String username = principal.getName();  // Get the logged-in user's username
        User existingUser = userService.findByUsername(username);  // Find the existing user by username

        // Update the allowed fields (username and password in this case)
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());  // Ensure to hash the password before saving

        userService.saveUser(existingUser, existingUser.getRoles());  // Save the updated user details
        return ResponseEntity.ok("User updated successfully");
    }

    // ADMIN: Edit any user's details
    @GetMapping("/users/all/{id}")
    @PreAuthorize("hasRole('ADMIN')")  // Only allow admins to access this endpoint
    public ResponseEntity<User> editUser(@PathVariable Long id) {
        User user = userService.getUserById(id);  // Find the user by their ID
        return ResponseEntity.ok(user);  // Return the user as JSON
    }

    // ADMIN: Update any user's details
    @PostMapping("/users/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")  // Only allow admins to access this endpoint
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user ) {

        User existingUser = userService.getUserById(id);  // Find the user by ID
        existingUser.setUsername(user.getUsername());  // Update the username
        existingUser.setPassword(user.getPassword());  // Ensure to hash the password before saving

        userService.saveUser(existingUser, existingUser.getRoles());  // Save the updated user details
        return ResponseEntity.ok("User updated successfully");
    }

    // ADMIN: Delete any user
    @PostMapping("/users/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")  // Only allow admins to delete users
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);  // Delete the user by ID
        return ResponseEntity.ok("User deleted successfully");
    }
}

