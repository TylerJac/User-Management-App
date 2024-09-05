package org.uma.uma.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.uma.uma.entity.Role;
import org.uma.uma.entity.User;
import org.uma.uma.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.getRoles());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
                               @ModelAttribute("role") String roleName, Model model) {
        // Check for validation errors
        if (result.hasErrors()) {
            model.addAttribute("roles", userService.getRoles());  // Add roles back to the model
            return "register";  // Show the registration form with validation errors
        }

        // Simplified role assignment using UserService's role management
        Role role = userService.getRoles().stream()
                .filter(r -> r.getName().equals(roleName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + roleName));

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        // Save user with assigned roles
        userService.saveUser(user, roles);
        return "redirect:/login?registered";  // Redirect after successful registration
    }
    @GetMapping("/login")
        public String login() {
            return "login";
            }

    @GetMapping("/users/all")
    public String viewAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "all-users";
    }
    @GetMapping("/user/details")
    public String showUserDetails(Model model, Principal principal) {
        String username = principal.getName(); // Fetch logged-in user's username
        User user = userService.findByUsername(username);

        model.addAttribute("user", user); // Add user to the model
        return "user-details"; // Return Thymeleaf template name
    }
    @PostMapping("/user/update")
    public String updateUserDetails(@Valid @ModelAttribute("user") User user, BindingResult result, Principal principal, Model model) {
        if (result.hasErrors()) {
            return "user-details";  // Return to user details page if there are errors
        }

        String username = principal.getName();
        User existingUser = userService.findByUsername(username);

        // Update only allowed fields (username, password, etc.)
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());  // Make sure to hash the password when saving

        userService.saveUser(existingUser, existingUser.getRoles());  // Save the updated user

        return "redirect:/user/details";  // Redirect back to user details page after update
    }

    // ADMIN: Edit any user's details
    @GetMapping("/users/all/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin-edit";  // Thymeleaf template for editing user
    }

    // ADMIN: Update any user's details
    @PostMapping("/users/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUser(@PathVariable Long id, @Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin-edit";  // Return to edit form if there are validation errors
        }

        User existingUser = userService.getUserById(id);
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());  // Make sure to hash the password when saving

        userService.saveUser(existingUser, existingUser.getRoles());  // Save the updated user

        return "redirect:/users/all";  // Redirect to the list of all users after updating
    }

    // ADMIN: Delete any user
    @PostMapping("/users/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/users/all";  // Redirect to user list after deletion
    }
}


