package com.demo.event_tickets.controller;

import com.demo.event_tickets.dto.LoginRequest;
import com.demo.event_tickets.dto.UserRegistrationRequest;
import com.demo.event_tickets.dto.UserRegistrationResponse;
import com.demo.event_tickets.model.Session;
import com.demo.event_tickets.model.User;
import com.demo.event_tickets.repository.SessionRepository;
import com.demo.event_tickets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {
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

    @PostMapping("/api/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request, HttpServletResponse response) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(request.getPassword())) {
                Session session = new Session();
                session.setUserId(user.getId());
                Session savedSession = sessionRepository.save(session);

                // Set-Cookie: SESSIONID=<session_id>;
                response.setHeader("Set-Cookie", String.format("SESSIONID=%s; HttpOnly; Max-Age=600; Path=/; SameSite=None", savedSession.getId()));

                return ResponseEntity.ok("{\"message\":\"Login successful\"}");
            }
        }
        return ResponseEntity.status(401).body("{\"error\":\"Invalid credentials\"}");
    }
}