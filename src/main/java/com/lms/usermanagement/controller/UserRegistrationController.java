package com.lms.usermanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.usermanagement.model.LoginRequest;
import com.lms.usermanagement.model.UserRegistrationRequest;
import com.lms.usermanagement.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserRegistrationController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        // Validate request
        if (request.getUsername() == null || request.getPassword() == null) {
            return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
        }

        // Register user using userService
        userService.registerUser(request);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate user and generate token
            String token = userService.loginUser(loginRequest);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Handle authentication failure
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
