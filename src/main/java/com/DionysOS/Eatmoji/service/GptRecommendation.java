package com.DionysOS.Eatmoji.service;

import com.DionysOS.Eatmoji.dto.EmotionRequest;
import com.DionysOS.Eatmoji.dto.FoodRecommend;
import com.DionysOS.Eatmoji.dto.RecommendResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;

@Service
public class GptRecommendation {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String gptUrl = "http://localhost:8000/gpt/recommendation";

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
}
