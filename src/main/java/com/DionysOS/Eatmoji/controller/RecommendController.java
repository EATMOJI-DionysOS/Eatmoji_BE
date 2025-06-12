package com.DionysOS.Eatmoji.controller;

import com.DionysOS.Eatmoji.dto.EmotionRequest;
import com.DionysOS.Eatmoji.dto.RecommendResponse;
import com.DionysOS.Eatmoji.dto.RecommendResponseWithID;
import com.DionysOS.Eatmoji.model.History;
import com.DionysOS.Eatmoji.model.User;
import com.DionysOS.Eatmoji.repository.HistoryRepository;
import com.DionysOS.Eatmoji.service.GptRecommendation;
import org.springframework.web.bind.annotation.*;
import com.DionysOS.Eatmoji.service.UserService;
import com.DionysOS.Eatmoji.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommend")
public class RecommendController {

    private final GptRecommendation recommendationService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;

    public RecommendController(GptRecommendation recommendationService, UserService userService, UserRepository userRepository, HistoryRepository historyRepository) {
        this.recommendationService = recommendationService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
    }

    @PostMapping("/emoji")
    public RecommendResponse recommend(@RequestBody EmotionRequest request) {
        String emoji = request.getEmoji();
        RecommendResponse response = recommendationService.printRecommendation(emoji);
        return response;
    }
    @PostMapping("/emoji/login")
    public RecommendResponse loginRecommend(@RequestBody EmotionRequest request) {
        String emoji = request.getEmoji();

        // 🟢 로그인한 사용자 이메일
        String email = userService.getCurrentUserEmail();

        // 🟢 사용자 프로필 정보
        Optional<User> profile = userRepository.findByEmail(email);
        List<String> categories = profile.map(User::getCategory).orElse(List.of());
        List<String> flavors = profile.map(User::getFlavor).orElse(List.of());
        List<String> diseases = profile.map(User::getDisease).orElse(List.of());
        List<String> allergies = profile.map(User::getAllergy).orElse(List.of());

        // 🟢 좋아요한 음식 목록
        List<History> likedHistories = historyRepository.findLikedFoodsByEmail(email);
        List<String> likedFoods = likedHistories.stream()
                .map(History::getFood)
                .distinct()
                .collect(Collectors.toList());

        // 🟢 FastAPI로 요청 및 결과 저장
        return recommendationService.getAndSaveEmojiPersonalizedRecommendation(
                emoji, email, categories, flavors, diseases, allergies, likedFoods
        );
    }

    @PostMapping("/personalized")
    public RecommendResponse recommendPersonalized() {
        String email = userService.getCurrentUserEmail();

        List<History> likedHistories = historyRepository.findLikedFoodsByEmail(email);
        List<String> likedFoods = likedHistories.stream()
                .map(History::getFood)
                .distinct()
                .collect(Collectors.toList());


        Optional<User> profile = userRepository.findByEmail(email);
        List<String> categories = profile.get().getCategory();
        List<String> flavors = profile.get().getFlavor();
        List<String> diseases = profile.get().getDisease();
        List<String> allergies = profile.get().getAllergy();
        RecommendResponse response = recommendationService.getAndSavePersonalizedRecommendation(
                email, categories, flavors, diseases, allergies, likedFoods
        );
        return response;
    }

}
