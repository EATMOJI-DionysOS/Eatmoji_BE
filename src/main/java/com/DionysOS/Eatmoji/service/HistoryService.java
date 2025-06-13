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
            try {
                // 💡 유효성 검사
                if (rec == null || rec.getFood() == null || rec.getReason() == null) {
                    System.err.println("⚠️ 추천 항목이 null이거나 필드가 누락됨: " + rec);
                    continue; // 이 항목은 건너뜀
                }

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

            } catch (Exception e) {
                System.err.println("❌ 서비스 내에서 히스토리 저장 중 예외 발생:");
                e.printStackTrace();
            }
        }

        return responseList;
    }

}
