package com.DionysOS.Eatmoji.service;

import com.DionysOS.Eatmoji.dto.UserProfileRequest;
import com.DionysOS.Eatmoji.model.User;
import com.DionysOS.Eatmoji.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final String email = "test@example.com";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(email);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetCurrentUserEmail() {
        String result = userService.getCurrentUserEmail();
        assertEquals(email, result);
    }

    @Test
    public void testUpdateUserProfile_overwritesValues() {
        User user = new User(email, "password123");
        user.setCategory(List.of("양식"));
        user.setFlavor(List.of("단맛"));
        user.setDisease(List.of("고혈압"));
        user.setAllergy(List.of("우유"));

        UserProfileRequest request = new UserProfileRequest();
        request.setCategory(List.of("한식"));
        request.setFlavor(List.of("짠맛"));
        request.setDisease(List.of());
        request.setAllergy(List.of("우유")); // 변경 없음

        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updateUserProfile(user, request);

        assertEquals(List.of("한식"), user.getCategory());
        assertEquals(List.of("짠맛"), user.getFlavor());
        assertTrue(user.getDisease() == null || user.getDisease().isEmpty());
        assertEquals(List.of("우유"), user.getAllergy());
    }

    @Test
    public void testUpdateUserProfile_addToEmpty() {
        User user = new User(email, "password123");

        assertTrue(user.getCategory() == null || user.getCategory().isEmpty());

        UserProfileRequest request = new UserProfileRequest();
        request.setCategory(List.of("한식"));

        userService.updateUserProfile(user, request);

        assertEquals(List.of("한식"), user.getCategory());
    }

    @Test
    public void testUpdateUserProfile_setAllFields() {
        User user = new User(email, "password123");

        UserProfileRequest request = new UserProfileRequest();
        request.setCategory(List.of("중식"));
        request.setFlavor(List.of("매운맛"));
        request.setDisease(List.of("당뇨병"));
        request.setAllergy(List.of("땅콩", "갑각류"));

        userService.updateUserProfile(user, request);

        assertEquals(List.of("중식"), user.getCategory());
        assertEquals(List.of("매운맛"), user.getFlavor());
        assertEquals(List.of("당뇨병"), user.getDisease());
        assertEquals(List.of("땅콩", "갑각류"), user.getAllergy());
    }
}
