package com.DionysOS.Eatmoji.controller;

import com.DionysOS.Eatmoji.dto.EmotionRequest;
import com.DionysOS.Eatmoji.dto.RecommendResponse;
import com.DionysOS.Eatmoji.dto.UserProfileRequest;
import com.DionysOS.Eatmoji.service.GptRecommendation;
import org.springframework.web.bind.annotation.*;


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
