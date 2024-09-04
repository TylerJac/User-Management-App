package org.uma.uma.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.uma.uma.service.UserService;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private UserService UserService;

}
