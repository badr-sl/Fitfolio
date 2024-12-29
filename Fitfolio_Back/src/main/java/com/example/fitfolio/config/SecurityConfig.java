package com.example.fitfolio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.fitfolio.component.JwtUtil;
import com.example.fitfolio.service.UserService;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserService accountService; // Service for managing user accounts
    private final JwtUtil jwtUtil;
    private final UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // Constructor to inject AccountService
    public SecurityConfig(UserService accountService, UserService userService, JwtUtil jwtUtil) {
        this.accountService = accountService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // Bean for UserDetailsService, which loads user details by username
    @Bean
    UserDetailsService userDetailsService() {
        return username -> accountService.loadUserByUsername(username); // Load user via AccountService
    }


    // Bean for AuthenticationManager, which handles user authentication
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Retrieve the authentication manager
    }



    // Bean for AuthenticationProvider, providing the authentication implementation
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); // Create an authentication provider
        authProvider.setUserDetailsService(userDetailsService()); // Set the user details service
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        //authProvider.setPasswordEncoder(passwordEncoder()); // Set the password encoder
        return authProvider; // Return the configured provider
    }

    // Bean for configuring the security filter chain
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()); // Disable CSRF
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())); // Enable CORS
        http.authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll() // Allow all requests
        );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Allow frontend origin
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allow HTTP methods
        configuration.setAllowedHeaders(List.of("*")); // Allow all headers
        configuration.setAllowCredentials(true); // Allow cookies and credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply CORS settings to all endpoints
        return source;
    }
}


