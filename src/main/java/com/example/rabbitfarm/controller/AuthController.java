package com.example.rabbitfarm.controller;

import com.example.rabbitfarm.model.User;
import com.example.rabbitfarm.repository.UserRepository; // Direct repository use for simplicity here
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken!");
        }

        // User newUser = new User();
        // newUser.setUsername(user.getUsername());
        // newUser.setPassword(passwordEncoder.encode(user.getPassword())); // Encode password
        // newUser.setRole(user.getRole() == null || user.getRole().isEmpty() ? "ROLE_USER" : user.getRole());
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword())); // Encode password
        newUser.setRole(user.getRole() == null || user.getRole().isEmpty() ? "ROLE_USER" : user.getRole());

        userRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully!");
    }
}
