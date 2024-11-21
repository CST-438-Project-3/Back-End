package com.cst438.project3.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.aot.generate.ValueCodeGenerator.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for testing purposes
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/newuser", "/login").permitAll() // Public endpoints
                        .anyRequest().authenticated() // Secure all other endpoints
                )
                .oauth2Login(withDefaults()) // Enable OAuth2 login
                .formLogin(withDefaults()); // Enable form-based login

        return http.build();
    }
}
