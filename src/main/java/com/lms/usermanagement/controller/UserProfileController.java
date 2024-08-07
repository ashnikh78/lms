package com.lms.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.usermanagement.model.User;
import com.lms.usermanagement.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @PutMapping("/{username}")
    public ResponseEntity<User> updateUserProfile(@PathVariable String username, @Valid @RequestBody User updatedUser) {
        User updated = userService.updateUserProfile(username, updatedUser);
        return ResponseEntity.ok(updated);
    }
}
