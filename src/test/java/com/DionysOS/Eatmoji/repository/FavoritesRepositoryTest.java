package com.DionysOS.Eatmoji.repository;

import com.DionysOS.Eatmoji.model.Favorites;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class FavoritesRepositoryTest {

    @Autowired
    private FavoritesRepository favoritesRepository;

    @AfterEach
    void tearDown() {
        favoritesRepository.deleteAll();
    }

    @Test
    @DisplayName("즐겨찾기 저장 및 이메일 기준 조회")
    void saveAndFindByEmail() {
        Favorites favorite = new Favorites(
                "user@example.com",
                "김치찌개"
        );

        favoritesRepository.save(favorite);
        List<Favorites> results = favoritesRepository.findByEmail("user@example.com");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFavorite()).isEqualTo("김치찌개");
    }
}
