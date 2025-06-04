package com.DionysOS.Eatmoji.service;

import com.DionysOS.Eatmoji.dto.EmotionRequest;
import com.DionysOS.Eatmoji.dto.FoodRecommend;
import com.DionysOS.Eatmoji.dto.RecommendResponse;

import com.DionysOS.Eatmoji.model.History;
import com.DionysOS.Eatmoji.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GptRecommendation {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String gptUrl = "http://localhost:8000/gpt/recommendation";
    private final HistoryRepository historyRepository;

    public GptRecommendation(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }
    @Autowired
    private UserService userService; // 사용자 서비스 주입

    public void printRecommendation(String emoji) {
        EmotionRequest request = new EmotionRequest();
        request.setEmoji(emoji);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmotionRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<RecommendResponse> response = restTemplate.exchange(
                gptUrl,
                HttpMethod.POST,
                entity,
                RecommendResponse.class
        );

        RecommendResponse body = response.getBody();
        if (body != null) {
            List<FoodRecommend> recs = body.getRecommendations();
            for (FoodRecommend rec : recs) {
                System.out.println("추천 음식: " + rec.getFood());
            }
        }
    }

    public void getAndSaveRecommendation(String emoji) {
        // 1. FastAPI 호출
        EmotionRequest request = new EmotionRequest();
        request.setEmoji(emoji);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmotionRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<RecommendResponse> response = restTemplate.exchange(
                gptUrl,
                HttpMethod.POST,
                entity,
                RecommendResponse.class
        );

        RecommendResponse body = response.getBody();
        if (body != null && body.getRecommendations() != null) {
            for (FoodRecommend rec : body.getRecommendations()) {
                // 2. History 저장
                History history = new History(
                        userService.getCurrentUserEmail(),      // TODO: 로그인 연동 시 대체
                        emoji,                      // 감정 or 이모지
                        rec.getFood(),
                        LocalDateTime.now(),
                        false
                );
                historyRepository.save(history);
            }
        }

    }


}
