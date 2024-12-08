package com.demo.event_tickets.controller;

import com.demo.event_tickets.dto.UserRegistrationRequest;
import com.demo.event_tickets.dto.UserRegistrationResponse;
import com.demo.event_tickets.model.User;
import com.demo.event_tickets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {
        // Basic email validation
        if (!request.getEmail().matches(".*@.*\\..*")) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setVendor(request.isVendor());

        try {
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(new UserRegistrationResponse(savedUser.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Username or email already exists");
        }
    }
}