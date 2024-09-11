package org.uma.uma.controller;
import jakarta.validation.Valid;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.uma.uma.entity.Role;
import org.uma.uma.entity.User;
import org.uma.uma.repository.UserRepository;
import org.uma.uma.service.JwtUserDetailsService;
import org.uma.uma.config.JwtTokenUtil;
import org.uma.uma.service.UserService;
import org.uma.uma.repository.RoleRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result, @RequestParam String roleName) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        // Find the role by roleName passed as a parameter
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + roleName));

        // Assign the role to the user
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        user.setUsername(Encode.forHtml(user.getUsername()));
        // Save the user along with their roles
        userService.saveUser(user, roles);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {

            // Authenticate the user using the username and password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Load user details from the database
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

            // Generate a JWT token for the authenticated user
            String token = jwtTokenUtil.generateToken(userDetails);

            // Return the token in a structured JSON response
            return ResponseEntity.ok(Collections.singletonMap("token", token));


        } catch (Exception e) {
            // If authentication fails, return 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }


    @GetMapping("/token")
    public ResponseEntity<?> generateToken(@RequestParam String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }
}

