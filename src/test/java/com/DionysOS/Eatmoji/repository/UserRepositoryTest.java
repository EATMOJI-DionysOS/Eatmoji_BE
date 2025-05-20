package com.DionysOS.Eatmoji.repository;

import com.DionysOS.Eatmoji.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("사용자 저장 및 이메일 조회")
    void saveAndFindUser() {
        User user = new User(
                "test@example.com",
                "1234"
        );

        userRepository.save(user);
        assertThat(userRepository.existsByEmail("test@example.com")).isTrue();
    }
}
