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

@Controller  // Marks this class as a Spring MVC controller
public class UserController {

    @Autowired
    private UserService userService;  // Inject the UserService to handle user operations

    // Display the registration form for new users
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());  // Add an empty User object to the model for form binding
        model.addAttribute("roles", userService.getRoles());  // Add roles to the model to populate role selection in the form
        return "register";  // Return the "register" Thymeleaf template
    }

    // Handle registration form submission
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
                               @ModelAttribute("role") String roleName, Model model) {
        // Check for validation errors in the user form data
        if (result.hasErrors()) {
            model.addAttribute("roles", userService.getRoles());  // Add role's back to the model to display them again
            return "register";  // Show the registration form again if there are validation errors
        }

        // Simplified role assignment using UserService's role management
        Role role = userService.getRoles().stream()
                .filter(r -> r.getName().equals(roleName))  // Find the selected role
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + roleName));  // Throw error if role is not valid

        Set<Role> roles = new HashSet<>();
        roles.add(role);  // Assign the selected role to the user

        // Save the user along with the assigned roles
        userService.saveUser(user, roles);
        return "redirect:/login?registered";  // Redirect to login page after successful registration
    }

    // Display the login page
    @GetMapping("/login")
    public String login() {
        return "login";  // Return the "login" Thymeleaf template
    }

    // View a list of all users (for admins)
    @GetMapping("/users/all")
    public String viewAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());  // Fetch all users and add them to the model
        return "all-users";  // Return the "all-users" Thymeleaf template
    }

    // Display details of the logged-in user
    @GetMapping("/user/details")
    public String showUserDetails(Model model, Principal principal) {
        String username = principal.getName();  // Get the username of the currently logged-in user
        User user = userService.findByUsername(username);  // Find the user by their username

        model.addAttribute("user", user);  // Add the user object to the model
        return "user-details";  // Return the "user-details" Thymeleaf template
    }

    // Handle updating the logged-in user's details
    @PostMapping("/user/update")
    public String updateUserDetails(@Valid @ModelAttribute("user") User user, BindingResult result, Principal principal) {
        if (result.hasErrors()) {
            return "user-details";  // Return to the user details page if there are validation errors
        }

        String username = principal.getName();  // Get the logged-in user's username
        User existingUser = userService.findByUsername(username);  // Find the existing user by username

        // Update the allowed fields (username and password in this case)
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());  // Ensure to hash the password when saving

        userService.saveUser(existingUser, existingUser.getRoles());  // Save the updated user details
        return "redirect:/user/details";  // Redirect back to the user details page after the update
    }

    // ADMIN: Edit any user's details
    @GetMapping("/users/all/{id}")
    @PreAuthorize("hasRole('ADMIN')")  // Only allow admins to access this endpoint
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);  // Find the user by their ID
        model.addAttribute("user", user);  // Add the user object to the model
        return "admin-edit";  // Return the "admin-edit" Thymeleaf template for editing the user
    }

    // ADMIN: Update any user's details
    @PostMapping("/users/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")  // Only allow admins to access this endpoint
    public String updateUser(@PathVariable Long id, @Valid @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "admin-edit";  // Return to the admin edit form if there are validation errors
        }

        User existingUser = userService.getUserById(id);  // Find the user by ID
        existingUser.setUsername(user.getUsername());  // Update the username
        existingUser.setPassword(user.getPassword());  // Ensure to hash the password when saving

        userService.saveUser(existingUser, existingUser.getRoles());  // Save the updated user details
        return "redirect:/users/all";  // Redirect to the list of all users after updating
    }

    // ADMIN: Delete any user
    @PostMapping("/users/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")  // Only allow admins to delete users
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);  // Delete the user by ID
        return "redirect:/users/all";  // Redirect to the list of users after deletion
    }
}
