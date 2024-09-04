package org.uma.uma.controller;

import org.uma.uma.entity.User;
import org.uma.uma.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult result, @RequestParam("role") String roleName) {
        if (result.hasErrors()) {
            return "register";
        }
        userService.saveUser(user, roleName);
        return "redirect:/login?registered";
    }

    @GetMapping("/users/all")
    public String viewAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "all-users";
    }
}

