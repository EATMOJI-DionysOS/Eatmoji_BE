package com.DionysOS.Eatmoji.service;

import com.DionysOS.Eatmoji.dto.UserProfileRequest;
import com.DionysOS.Eatmoji.model.User;
import com.DionysOS.Eatmoji.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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


    public void updateUserProfile(User user, UserProfileRequest request) {
        if (request.getCategory() != null) {
            user.setCategory(new ArrayList<>(request.getCategory()));
        }

        if (request.getFlavor() != null) {
            user.setFlavor(new ArrayList<>(request.getFlavor()));
        }

        if (request.getDisease() != null) {
            user.setDisease(new ArrayList<>(request.getDisease()));
        }

        if (request.getAllergy() != null) {
            user.setAllergy(new ArrayList<>(request.getAllergy()));
        }
        userRepository.save(user);

    }


}