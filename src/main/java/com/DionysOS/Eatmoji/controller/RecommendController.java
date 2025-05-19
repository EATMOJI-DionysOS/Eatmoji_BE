package com.DionysOS.Eatmoji.controller;

import com.DionysOS.Eatmoji.service.GptRecommendation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommend")
public class RecommendController {

    private final GptRecommendation recommendationService;

    public RecommendController(GptRecommendation recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping
    public void recommend(@RequestBody String emoji) {
        recommendationService.printRecommendation(emoji);
    }
}
