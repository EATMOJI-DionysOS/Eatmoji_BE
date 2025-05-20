package com.DionysOS.Eatmoji.repository;

import com.DionysOS.Eatmoji.model.Favorites;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritesRepository extends MongoRepository<Favorites, String> {
    List<Favorites> findByEmail(String email); // 사용자별 즐겨찾기 조회
}