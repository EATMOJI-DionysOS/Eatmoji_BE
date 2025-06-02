package com.DionysOS.Eatmoji.controller;

import com.DionysOS.Eatmoji.dto.SignupRequest;
import com.DionysOS.Eatmoji.model.User;
import com.DionysOS.Eatmoji.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() { userRepository.deleteAll(); } // cleaning up before each test.

    @Test
    void testSignupSuccess() throws Exception { // expected to succeed the sign up process
        SignupRequest request = new SignupRequest();
        request.setEmail("test123@example.com");
        request.setPassword("password123");


        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test123@example.com"))
                .andExpect(jsonPath("$.message").value("Signup successful!"));
    }

    @Test
    void testSignupDuplicateEmail() throws Exception {
        // First signup
        userRepository.save(new User("test12@example.com", passwordEncoder.encode("pass123")));

        // Attempt to signup with the same email
        SignupRequest request = new SignupRequest();
        request.setEmail("test12@example.com");
        request.setPassword("pass123");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Email already exists")));
    }

    @Test
    void testLoginSuccess() throws Exception {
        userRepository.save(new User("test333@example.com", passwordEncoder.encode("password123")));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test333@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test333@example.com"))
                .andExpect(jsonPath("$.message").value("Login successful!"));
    }

    @Test
    void testLoginInvalidPassword() throws Exception {
        userRepository.save(new User("test4444@example.com", passwordEncoder.encode("correctpassword1")));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test4444@example.com\",\"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid password"));
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"123123@example.com\",\"password\":\"anypassword123\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }
}
