package com.lms.usermanagement.service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lms.usermanagement.model.LoginRequest;
import com.lms.usermanagement.model.User;
import com.lms.usermanagement.model.UserRegistrationRequest;
import com.lms.usermanagement.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Key key;

    // Initialize the key
    public UserService() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // Method to register a user
    public void registerUser(UserRegistrationRequest userRegistrationRequest) {
        // Create a User object from the registration request
        User user = new User(
                userRegistrationRequest.getUsername(),
                passwordEncoder.encode(userRegistrationRequest.getPassword()), // Encrypt password
                userRegistrationRequest.getFullName(),
                userRegistrationRequest.getEmail()
        );
        
        // Save the user to the database
        userRepository.save(user);
    }
      public User updateUserProfile(String username, User updatedUser) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFullName(updatedUser.getFullName());
            user.setEmail(updatedUser.getEmail());
            // Update other fields as necessary
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
    // Method to authenticate a user
   public String loginUser(LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return generateToken(user);
            } else {
                throw new RuntimeException("Invalid credentials");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // Method to generate a JWT token
    protected String generateToken(User user) {
        return Jwts.builder()
                   .setSubject(user.getUsername())
                   .claim("fullName", user.getFullName())
                   .claim("email", user.getEmail())
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiration
                   .signWith(key)
                   .compact();
    }
      public String getTokenForUser(User user) {
        return generateToken(user);
    }
}
