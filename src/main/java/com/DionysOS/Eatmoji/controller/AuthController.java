package com.DionysOS.Eatmoji.controller;

import com.DionysOS.Eatmoji.dto.*;
import com.DionysOS.Eatmoji.model.User;
import com.DionysOS.Eatmoji.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User(request.getEmail(), request.getPassword());

        userRepository.save(user);
        return ResponseEntity.ok(SignupResponse.builder().email(user.getEmail()).message("Signup successful!").build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .map(user -> {
                    if (user.getPassword().equals(request.getPassword())) {
                        return ResponseEntity.ok(LoginResponse.builder().email(user.getEmail()).message("Login successful!").build());
                    } else {
                        return ResponseEntity.status(401).body("Invalid password");
                    }
                }).orElse(ResponseEntity.status(404).body("User not found"));
    }

}
