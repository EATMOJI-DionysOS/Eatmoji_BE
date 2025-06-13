package com.DionysOS.Eatmoji.service;

import com.DionysOS.Eatmoji.dto.FoodRecommend;
import com.DionysOS.Eatmoji.dto.HistoryResponse;
import com.DionysOS.Eatmoji.dto.RecommendResponse;

import com.DionysOS.Eatmoji.dto.RecommendResponseWithID;
import com.DionysOS.Eatmoji.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.List;

@Service
public class GptRecommendation {
    private final RestTemplate restTemplate;
    private final HistoryRepository historyRepository;

    @Autowired
    private HistoryService historyService;


    public GptRecommendation(HistoryRepository historyRepository) {
        this.restTemplate = new RestTemplate();
        this.historyRepository = historyRepository;
    }
    // 내부 history/save api 호출
    public String saveRecommendationHistory(String email, String emotion, List<FoodRecommend> recommendations) {
        try {
            List<HistoryResponse> responseList = historyService.saveRecommendation(email, emotion, recommendations);
            if (!responseList.isEmpty()) {
                return responseList.get(0).getId(); // 첫 번째 히스토리 ID 반환
            }
        } catch (Exception e) {
            System.err.println("❌ 히스토리 저장 실패:");
            e.printStackTrace();
        }
        return "";
    }

    public RecommendResponse printRecommendation(String emoji) {
        Map<String, String> requestBody = Map.of("emoji", emoji);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<RecommendResponse> response = restTemplate.exchange(
                "http://localhost:8000/gpt/recommend",
                HttpMethod.POST,
                entity,
                RecommendResponse.class
        );

        return response.getBody();

    }

    public RecommendResponseWithID getAndSaveEmojiPersonalizedRecommendation(
            String emoji,
            String email,
            List<String> categories,
            List<String> flavors,
            List<String> diseases,
            List<String> allergies,
            List<String> likedFoods
    ) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("❌ 이메일이 null 또는 빈 문자열입니다.");
        }

        // FastAPI 요청 body 구성
        Map<String, Object> requestBody = Map.of(
                "emoji", emoji,
                "email", email,
                "category", categories,
                "flavor", flavors,
                "disease", diseases,
                "allergy", allergies,
                "likedFoods", likedFoods
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // FastAPI 호출
        String fastapiUrl = "http://localhost:8000/gpt/recommend/login";
        ResponseEntity<RecommendResponse> response = restTemplate.exchange(
                fastapiUrl,
                HttpMethod.POST,
                entity,
                RecommendResponse.class
        );

        if (response.getBody() == null) {
            throw new RuntimeException("GPT 응답이 null입니다.");
        }


        RecommendResponse body = response.getBody();

        if (body.getRecommendations() == null || body.getRecommendations().isEmpty()) {
            throw new RuntimeException("GPT 응답의 추천 리스트가 비어있습니다.");
        }


        String id = "";
        try {
            id = saveRecommendationHistory(email, emoji, body.getRecommendations());
        } catch (Exception e) {
            System.err.println("❌ 히스토리 저장 중 오류:");
            e.printStackTrace();
        }

        RecommendResponseWithID result = new RecommendResponseWithID();
        result.setEmotion(emoji);
        result.setRecommendations(body.getRecommendations());
        result.setHistoryId(id);

        return result;
    }

    public RecommendResponseWithID getAndSavePersonalizedRecommendation(String email,
                                                                  List<String> categories,
                                                                  List<String> flavors,
                                                                  List<String> diseases,
                                                                  List<String> allergies,
                                                                  List<String> likedFoods) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("❌ 이메일이 null이거나 빈 문자열입니다.");
        }

        // 1. GPT API 요청 데이터
        Map<String, Object> requestBody = Map.of(
                "email", email,
                "category", categories,
                "flavor", flavors,
                "disease", diseases,
                "allergy", allergies,
                "likedFoods", likedFoods
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        RecommendResponse body;
        try {
            // 2. GPT API 호출
            String personalizedUrl = "http://localhost:8000/gpt/recommend/personalized";
            ResponseEntity<RecommendResponse> response = restTemplate.exchange(
                    personalizedUrl,
                    HttpMethod.POST,
                    entity,
                    RecommendResponse.class
            );

            body = response.getBody();
            if (body == null) {
                throw new RuntimeException("❌ GPT 응답이 null입니다.");
            }
            if (body.getRecommendations() == null || body.getRecommendations().isEmpty()) {
                throw new RuntimeException("❌ GPT 응답의 추천 리스트가 비어있습니다.");
            }
        } catch (Exception e) {
            System.err.println("❌ GPT Personalized API 호출 실패:");
            e.printStackTrace();
            throw new RuntimeException("GPT Personalized API 호출 실패", e);
        }

        // 3. 히스토리 저장
        String id = "";
        try {
            id = saveRecommendationHistory(email, "\uD83D\uDC38오메추", body.getRecommendations());
        } catch (Exception e) {
            System.err.println("❌ 히스토리 저장 중 오류:");
            e.printStackTrace();
        }

        // 4. 최종 응답 구성
        RecommendResponseWithID result = new RecommendResponseWithID();
        result.setEmotion("\uD83D\uDC38오메추");
        result.setRecommendations(body.getRecommendations());
        result.setHistoryId(id);

        return result;

    }

}
