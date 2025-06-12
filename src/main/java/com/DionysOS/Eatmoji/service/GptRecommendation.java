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

    @Autowired
    private UserService userService; // 사용자 서비스 주입

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


        RecommendResponse body = response.getBody();
        assert body != null;
        String id = saveRecommendationHistory(email, body.getEmotion(), body.getRecommendations());
        RecommendResponseWithID result = new RecommendResponseWithID();
        result.setEmotion(body.getEmotion());
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

        // 2. GPT API 호출
        String personalizedUrl = "http://localhost:8000/gpt/recommend/personalized";
        ResponseEntity<RecommendResponse> response = restTemplate.exchange(
                personalizedUrl,
                HttpMethod.POST,
                entity,
                RecommendResponse.class
        );

        RecommendResponse body = response.getBody();
        ZonedDateTime nowInKST = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime localKST = nowInKST.toLocalDateTime();


        assert body != null;
        String id = saveRecommendationHistory(email, body.getEmotion(), body.getRecommendations());
        RecommendResponseWithID result = new RecommendResponseWithID();
        result.setEmotion(body.getEmotion());
        result.setRecommendations(body.getRecommendations());
        result.setHistoryId(id);

        return result;

    }



}
