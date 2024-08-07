package com.lms.usermanagement.config;

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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/login", "/public/**").permitAll() // Public endpoints
                    .requestMatchers("/admin/**").hasRole("ADMIN") // Restrict access based on roles
                    .anyRequest().authenticated() // All other requests require authentication
            )
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/login") // Specify the login page
                    .defaultSuccessUrl("/dashboard") // Redirect after successful login
                    .permitAll()
            )
            .logout(logout ->
                logout
                    .logoutUrl("/logout") // Specify the logout URL
                    .logoutSuccessUrl("/login?logout") // Redirect after successful logout
                    .permitAll()
            )
            .csrf(csrf -> csrf.disable()); // Disable CSRF for simplicity in development (not recommended for production)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
