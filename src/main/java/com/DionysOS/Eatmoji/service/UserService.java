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


    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Get current authenticated user's email
    public String getCurrentUserEmail() {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null) {
                throw new IllegalStateException("❌ 인증 정보가 존재하지 않습니다 (SecurityContext가 비어 있음).");
            }

            Object principal = auth.getPrincipal();

            if (principal instanceof String token && token.startsWith("Bearer ")) {
                // Bearer token 직접 추출
                String jwt = token.substring(7);
                return jwtTokenProvider.getEmailFromToken(jwt);
            }

            if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
                return userDetails.getUsername(); // 기본 전략 fallback
            }

            // 그 외의 경우에도 fallback
            return principal.toString();

        } catch (Exception e) {
            System.err.println("❌ 현재 사용자 이메일 추출 실패:");
            e.printStackTrace();
            throw new RuntimeException("현재 로그인된 사용자의 이메일을 가져올 수 없습니다.", e);
        }
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