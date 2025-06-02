package com.DionysOS.Eatmoji.service;

import com.DionysOS.Eatmoji.dto.AttributeUpdateRequest;
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

        // Mock SecurityContextHolder
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
    public void testUpdateUserProfile() {
        User user = new User(email, "password123");

        user.setCategory(List.of("양식"));
        user.setFlavor(List.of("단맛"));
        user.setDisease(List.of("고혈압"));
        user.setAllergy(List.of("우유"));

        // 기본 생성자 사용 후 setter로 값 설정
        AttributeUpdateRequest categoryAttr = new AttributeUpdateRequest();
        categoryAttr.setAdd(List.of("한식"));
        categoryAttr.setRemove(List.of("양식"));

        AttributeUpdateRequest flavorAttr = new AttributeUpdateRequest();
        flavorAttr.setAdd(List.of("짠맛"));
        flavorAttr.setRemove(List.of("단맛"));

        AttributeUpdateRequest diseaseAttr = new AttributeUpdateRequest();
        diseaseAttr.setAdd(null);
        diseaseAttr.setRemove(List.of("고혈압"));

        AttributeUpdateRequest allergyAttr = new AttributeUpdateRequest(); // 유지할 것이므로 비워둠

        UserProfileRequest request = new UserProfileRequest();
        request.setCategory(categoryAttr);
        request.setFlavor(flavorAttr);
        request.setDisease(diseaseAttr);
        request.setAllergy(allergyAttr);

        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updateUserProfile(user, request);

        assertEquals(List.of("한식"), user.getCategory());
        assertEquals(List.of("짠맛"), user.getFlavor());
        assertNull(user.getDisease());  // 완전히 제거됨
        assertEquals(List.of("우유"), user.getAllergy()); // 변경 없음
    }

    @Test
    public void testAddAttributesWhenEmpty() {
        User user = new User(email, "password123");

        // 초기 상태는 모두 null
        assertNull(user.getCategory());

        AttributeUpdateRequest categoryAttr = new AttributeUpdateRequest();
        categoryAttr.setAdd(List.of("한식"));  // 처음 추가

        UserProfileRequest request = new UserProfileRequest();
        request.setCategory(categoryAttr);

        userService.updateUserProfile(user, request);

        assertEquals(List.of("한식"), user.getCategory());
    }

    @Test
    public void testAddAndRemoveSameItem() {
        User user = new User(email, "password123");
        user.setFlavor(List.of("매운맛"));

        AttributeUpdateRequest flavorAttr = new AttributeUpdateRequest();
        flavorAttr.setAdd(List.of("단맛"));      // 추가
        flavorAttr.setRemove(List.of("단맛"));   // 동시에 제거

        UserProfileRequest request = new UserProfileRequest();
        request.setFlavor(flavorAttr);

        userService.updateUserProfile(user, request);

        // 단맛은 제거되고 매운맛만 남아야 함
        assertEquals(List.of("매운맛"), user.getFlavor());
    }


}
