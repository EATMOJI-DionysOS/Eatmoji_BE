package com.DionysOS.Eatmoji.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class HistoryResponse {
    private String id; // MongoDB's history's ID
    private String email;
    private String emotion;
    private FoodRecommend recommendation;
    private LocalDateTime createdAt;
    private boolean isLiked;
}
