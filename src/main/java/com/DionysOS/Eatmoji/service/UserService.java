package com.DionysOS.Eatmoji.service;

import com.DionysOS.Eatmoji.model.User;
import com.DionysOS.Eatmoji.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Get current authenticated user's email
    public String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString(); // fallback
    }

    // Get full User object for currently authenticated user
    public Optional<User> getCurrentUser() {
        String email = getCurrentUserEmail();
        return userRepository.findByEmail(email);
    }
}