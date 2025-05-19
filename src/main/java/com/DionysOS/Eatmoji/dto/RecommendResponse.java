package com.DionysOS.Eatmoji.dto;

import lombok.Data;
import java.util.List;

@Data
public class RecommendResponse {
    private String emotion;
    private double intensity;
    private List<FoodRecommend> recommendations;
}
