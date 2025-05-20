package com.DionysOS.Eatmoji.repository;


import com.DionysOS.Eatmoji.model.History;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends MongoRepository<History, String> {
    List<History> findByEmail(String email); // 사용자별 히스토리 조회
}