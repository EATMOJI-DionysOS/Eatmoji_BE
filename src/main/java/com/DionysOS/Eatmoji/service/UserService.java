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


    private List<String> merge(List<String> original, AttributeUpdateRequest update) {
        List<String> result = original == null ? new ArrayList<>() : new ArrayList<>(original);
        if (update != null) {
            if (update.getAdd() != null) result.addAll(update.getAdd());
            if (update.getRemove() != null) result.removeAll(update.getRemove());
        }
        return result.isEmpty() ? null : result;
    }

    public void updateUserProfile(User user, UserProfileRequest request) {
        user.setCategory(merge(user.getCategory(), request.getCategory()));
        user.setFlavor(merge(user.getFlavor(), request.getFlavor()));
        user.setDisease(merge(user.getDisease(), request.getDisease()));
        user.setAllergy(merge(user.getAllergy(), request.getAllergy()));

        userRepository.save(user);
    }
}