package com.DionysOS.Eatmoji.service;

import com.DionysOS.Eatmoji.dto.FoodRecommend;
import com.DionysOS.Eatmoji.dto.HistoryResponse;
import com.DionysOS.Eatmoji.model.History;
import com.DionysOS.Eatmoji.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<HistoryResponse> saveRecommendation(String email, String emotion, List<FoodRecommend> recommendations) {
        List<HistoryResponse> responseList = new ArrayList<>();

        for (FoodRecommend rec : recommendations) {
            History history = new History(
                    email,
                    emotion,
                    rec.getFood(),
                    rec.getReason(),
                    LocalDateTime.now(),
                    false
            );

            History saved = historyRepository.save(history);

            // DTO 변환
            HistoryResponse dto = new HistoryResponse();
            dto.setId(saved.getId());
            dto.setEmail(saved.getEmail());
            dto.setEmotion(saved.getEmotion());
            dto.setCreatedAt(saved.getCreatedAt());
            dto.setLiked(saved.isLiked());

            FoodRecommend dtoRec = new FoodRecommend();
            dtoRec.setFood(saved.getFood());
            dtoRec.setReason(saved.getReason());
            dto.setRecommendation(dtoRec);

            responseList.add(dto);
        }

        return responseList;
    }
}
