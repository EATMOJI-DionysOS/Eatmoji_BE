package com.DionysOS.Eatmoji.repository;

import com.DionysOS.Eatmoji.model.History;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class HistoryRepositoryTest {

    @Autowired
    private HistoryRepository historyRepository;

    @AfterEach
    void tearDown() {
        historyRepository.deleteAll();
    }

    @Test
    @DisplayName("히스토리 저장 및 이메일 기준 조회")
    void saveAndFindByEmail() {
        History history = new History(
                "user@example.com",
                "기쁨",
                "비빔밥",
                LocalDateTime.now(),
                false
        );

        historyRepository.save(history);
        List<History> results = historyRepository.findByEmail("user@example.com");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFood()).isEqualTo("비빔밥");
    }
}
