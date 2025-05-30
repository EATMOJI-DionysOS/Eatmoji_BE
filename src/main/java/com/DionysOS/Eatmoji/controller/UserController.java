package com.DionysOS.Eatmoji.controller;


import com.DionysOS.Eatmoji.dto.UserProfileRequest;
import com.DionysOS.Eatmoji.dto.UserProfileResponse;
import com.DionysOS.Eatmoji.repository.UserRepository;
import com.DionysOS.Eatmoji.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername(); // return the email
        }
        return principal.toString(); // fallback
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        String email = getCurrentUserEmail();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();

        User user = userOpt.get();
        return ResponseEntity.ok(UserProfileResponse.builder()
                        .email(user.getEmail())
                        .category(user.getCategory())
                        .flavor(user.getFlavor())
                        .disease(user.getDisease())
                        .allergy(user.getAllergy())
                        .build()
                );
    }

    @PatchMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UserProfileRequest request) {
        String email = getCurrentUserEmail();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();

        User user = userOpt.get();
        user.setCategory(request.getCategory());
        user.setFlavor(request.getFlavor());
        user.setDisease(request.getDisease());
        user.setAllergy(request.getAllergy());

        userRepository.save(user);
        return ResponseEntity.ok("User Profile updated successfully!");
    }
}
