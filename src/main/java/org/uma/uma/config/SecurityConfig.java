package org.uma.uma.config;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Injecting custom UserDetailsService for authentication logic

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Configures the password encoder to use BCrypt, a strong hashing algorithm
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disables CSRF protection (cross-site request forgery). For stateless APIs or specific use cases, this might be necessary.
                .csrf(AbstractHttpConfigurer::disable)

                // Configuring authorization for different endpoints
                .authorizeHttpRequests(authz -> authz
                        // Permit all users (authenticated or not) to access the root, registration, login, CSS, and JS files
                        .requestMatchers("/", "/register", "/login", "/css/**", "/js/**").permitAll()

                        // Only users with the ADMIN role can access the '/users/all' page
                        .requestMatchers("/users/all").hasRole("ADMIN")

                        // Both ADMIN and USER roles can access '/users/details'
                        .requestMatchers("/users/details").hasAnyRole("ADMIN", "USER")

                        // General access control for other user-specific URLs, allowing both ADMIN and USER roles
                        .requestMatchers("/users/").hasAnyRole("ADMIN", "USER")

                        // All other requests require authentication
                        .anyRequest().authenticated())

                // Configures form-based login
                .formLogin(form -> form
                        // Custom login page URL
                        .loginPage("/login")

                        // Redirect the user to '/user/details' after successful login
                        .defaultSuccessUrl("/user/details", true)
                        .permitAll())  // Allow anyone to access the login page

                // Configures logout behavior
                .logout(logout -> logout
                        // After logging out, redirect to the login page with a logout message
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());  // Permit anyone to trigger a logout

        return http.build();  // Build the SecurityFilterChain object based on the configured rules
    }
}


