package com.DionysOS.Eatmoji.service;

import com.DionysOS.Eatmoji.dto.AttributeUpdateRequest;
import com.DionysOS.Eatmoji.dto.UserProfileRequest;
import com.DionysOS.Eatmoji.model.User;
import com.DionysOS.Eatmoji.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
            if (request.getCategory().getAdd() != null) {
                user.getCategory().addAll(request.getCategory().getAdd());
            }
            if (request.getCategory().getRemove() != null) {
                user.getCategory().removeAll(request.getCategory().getRemove());
            }
        }

        if (request.getFlavor() != null) {
            if (request.getFlavor().getAdd() != null) {
                user.getFlavor().addAll(request.getFlavor().getAdd());
            }
            if (request.getFlavor().getRemove() != null) {
                user.getFlavor().removeAll(request.getFlavor().getRemove());
            }
        }

        if (request.getDisease() != null) {
            if (request.getDisease().getAdd() != null) {
                user.getDisease().addAll(request.getDisease().getAdd());
            }
            if (request.getDisease().getRemove() != null) {
                user.getDisease().removeAll(request.getDisease().getRemove());
            }
        }

        if (request.getAllergy() != null) {
            if (request.getAllergy().getAdd() != null) {
                user.getAllergy().addAll(request.getAllergy().getAdd());
            }
            if (request.getAllergy().getRemove() != null) {
                user.getAllergy().removeAll(request.getAllergy().getRemove());
            }
        }

        userRepository.save(user);
    }

}