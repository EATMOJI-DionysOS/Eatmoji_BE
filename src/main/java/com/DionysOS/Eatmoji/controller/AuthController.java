package com.DionysOS.Eatmoji.controller;

import com.DionysOS.Eatmoji.dto.*;
import com.DionysOS.Eatmoji.model.User;
import com.DionysOS.Eatmoji.repository.UserRepository;
import com.DionysOS.Eatmoji.service.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists! Please use a different email.");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(request.getEmail(), hashedPassword);

        userRepository.save(user);
        return ResponseEntity.ok(SignupResponse.builder().email(user.getEmail()).message("Signup successful!").build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isPresent() && passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {

            // Generate JWT tokens (access and refresh)
            String accessToken = jwtTokenProvider.createAccessToken(user.get().getEmail());
            String refreshToken = jwtTokenProvider.createRefreshToken(user.get().getEmail());

            // Store in Spring Security context
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user.get().getEmail(), null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authToken);

            // Also set session (so it persists in future requests)
            httpRequest.getSession(true); // create session

            return ResponseEntity.ok(LoginResponse.builder()
                    .email(user.get().getEmail())
                    .message("Login successful!")
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build());
        }

        return ResponseEntity.status(401).body("Invalid email or password! Please try again.");
    }


}
