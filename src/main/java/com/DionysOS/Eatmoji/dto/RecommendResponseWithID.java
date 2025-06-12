package com.DionysOS.Eatmoji.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecommendResponseWithID {
    private String emotion;
    private List<FoodRecommend> recommendations;
    private String historyId;  // 저장된 히스토리 ID
}