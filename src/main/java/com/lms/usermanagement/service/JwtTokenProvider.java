package com.lms.usermanagement.service;

import com.lms.usermanagement.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import jakarta.annotation.PostConstruct;

import java.util.Date;

import java.util.Date;

@Service
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private String jwtExpirationString; // Store as String first

    private long jwtExpiration; // Converted value

    @PostConstruct
    private void init() {
        try {
            // Convert the String to a long value
            this.jwtExpiration = Long.parseLong(jwtExpirationString.replaceAll("[^\\d]", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid jwt.expiration value: " + jwtExpirationString, e);
        }
    }

    public String generateToken(User user) {
        // Create the JWT token with expiration
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}