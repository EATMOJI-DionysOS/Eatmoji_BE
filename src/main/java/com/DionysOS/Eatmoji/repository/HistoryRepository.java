package com.DionysOS.Eatmoji.repository;


import com.DionysOS.Eatmoji.model.History;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends MongoRepository<History, String> {
    List<History> findByEmail(String email); // 사용자별 히스토리 조회

    @Query(value = "{ 'email': ?0, 'isLiked': true }", fields = "{ 'food' : 1 }")
    List<History> findLikedFoodsByEmail(@Param("email")String email);
}