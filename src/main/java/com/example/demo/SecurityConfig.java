package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Provides a PasswordEncoder bean (BCrypt) for hashing and verifying
 * user passwords. This does NOT enable Spring Security's filter chain,
 * authentication, or login pages - it only exposes the encoder so
 * UserService can hash/verify passwords. Existing session-based auth
 * continues to work as before.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}