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
import java.util.List;

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
        ZonedDateTime nowInKST = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime localKST = nowInKST.toLocalDateTime();

        if (body != null && body.getRecommendations() != null) {
            for (FoodRecommend rec : body.getRecommendations()) {
                try {
                    // ✅ 1. 현재 사용자 이메일 확인 로그
                    String email = userService.getCurrentUserEmail();
                    System.out.println("Current user email: " + email);

                    // ✅ 2. History 객체 생성
                    History history = new History(
                            email,
                            emoji,
                            rec.getFood(),
                            rec.getReason(),
                            localKST,
                            false
                    );

                    // ✅ 3. 저장 시도 및 확인 로그
                    History saved = historyRepository.save(history);
                    System.out.println("Saved history: " + saved);
                } catch (Exception e) {
                    // ✅ 4. 예외 로그
                    System.err.println("Error while saving history:");
                    e.printStackTrace();
                }
            }
        } else {
            System.err.println("No recommendation received from GPT.");
        }
        return response.getBody();
    }

    public RecommendResponse getAndSaveEmojiPersonalizedRecommendation(
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
        if (body != null && body.getRecommendations() != null) {
            ZonedDateTime nowInKST = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
            LocalDateTime localKST = nowInKST.toLocalDateTime();
            for (FoodRecommend rec : body.getRecommendations()) {
                History history = new History(
                        email,
                        emoji,
                        rec.getFood(),
                        rec.getReason(),
                        localKST,
                        false
                );
                historyRepository.save(history);
            }
        }
        return body;
    }
    public RecommendResponse getAndSavePersonalizedRecommendation(String email,
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
        String personalizedUrl = "http://localhost:8000/api/personalized-recommend";
        ResponseEntity<RecommendResponse> response = restTemplate.exchange(
                personalizedUrl,
                HttpMethod.POST,
                entity,
                RecommendResponse.class
        );

        RecommendResponse body = response.getBody();
        ZonedDateTime nowInKST = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime localKST = nowInKST.toLocalDateTime();

        // 3. 결과 저장
        if (body != null && body.getRecommendations() != null) {
            for (FoodRecommend rec : body.getRecommendations()) {
                try {
                    // 추천된 음식마다 DB 저장
                    History history = new History(
                            email,
                            "personalized",  // 이모지 대신 'personalized'로
                            rec.getFood(),
                            rec.getReason(),
                            localKST,
                            false
                    );

                    History saved = historyRepository.save(history);
                    System.out.println("Saved personalized history: " + saved);
                } catch (Exception e) {
                    System.err.println("Error while saving personalized history:");
                    e.printStackTrace();
                }
            }
        } else {
            System.err.println("No personalized recommendation received from GPT.");
        }

        return body;
    }



}
