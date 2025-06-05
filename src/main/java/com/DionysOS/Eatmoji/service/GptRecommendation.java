package com.DionysOS.Eatmoji.service;

import com.DionysOS.Eatmoji.dto.FoodRecommend;
import com.DionysOS.Eatmoji.dto.RecommendResponse;

import com.DionysOS.Eatmoji.model.History;
import com.DionysOS.Eatmoji.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

@Service
public class GptRecommendation {
    private final RestTemplate restTemplate;
    private final String gptUrl = "http://localhost:8000/gpt/recommendation";
    private final HistoryRepository historyRepository;


    public GptRecommendation(HistoryRepository historyRepository) {
        this.restTemplate = new RestTemplate();
        this.historyRepository = historyRepository;
    }
    @Autowired
    private UserService userService; // 사용자 서비스 주입

    public RecommendResponse printRecommendation(String emoji) {
        Map<String, String> requestBody = Map.of("emoji", emoji);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

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
