package org.uma.uma.controller;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.uma.uma.entity.Role;
import org.uma.uma.entity.User;
import org.uma.uma.service.UserService;
import jakarta.validation.Valid;
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
    public String registerUser(@ModelAttribute("user") User user, @ModelAttribute("role") String roleName) {
        // Simplified role assignment using UserService's role management
        Role role = userService.getRoles().stream()
                .filter(r -> r.getName().equals(roleName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + roleName));

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        userService.saveUser(user, roles);
        return "redirect:/login?registered";
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

}

