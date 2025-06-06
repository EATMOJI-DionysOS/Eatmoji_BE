package com.DionysOS.Eatmoji.controller;

import com.DionysOS.Eatmoji.dto.EmotionRequest;
import com.DionysOS.Eatmoji.dto.RecommendResponse;
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
