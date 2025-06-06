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

    public RecommendController(GptRecommendation recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping("/emoji")
    public RecommendResponse recommend(@RequestBody EmotionRequest request) {
        String emoji = request.getEmoji();
        RecommendResponse response = recommendationService.printRecommendation(emoji);
        return response;
    }

}
