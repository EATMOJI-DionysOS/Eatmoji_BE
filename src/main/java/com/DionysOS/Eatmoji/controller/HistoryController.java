package com.DionysOS.Eatmoji.controller;

import com.DionysOS.Eatmoji.dto.FoodRecommend;
import com.DionysOS.Eatmoji.dto.HistoryResponse;
import com.DionysOS.Eatmoji.dto.RecommendResponse;
import com.DionysOS.Eatmoji.model.History;
import com.DionysOS.Eatmoji.repository.HistoryRepository;
import com.DionysOS.Eatmoji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private UserService userService;

    private final HistoryRepository historyRepository;

    public HistoryController(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }


    // 이메일로 히스토리 조회
    // History 조회 시 DTO로 반환
    @GetMapping
    public ResponseEntity<List<HistoryResponse>> getHistories() {
        List<History> histories = historyRepository.findByEmail(userService.getCurrentUserEmail());

        List<HistoryResponse> responses = histories.stream().map(history -> {
            HistoryResponse dto = new HistoryResponse();
            dto.setId(history.getId());
            dto.setEmail(history.getEmail());
            dto.setEmotion(history.getEmotion());
            dto.setCreatedAt(history.getCreatedAt());
            dto.setLiked(history.isLiked());

            FoodRecommend rec = new FoodRecommend();
            rec.setFood(history.getFood());
            rec.setReason(history.getReason());
            dto.setRecommendation(rec);

            return dto;
        }).toList();

        return ResponseEntity.ok(responses);
    }

    // 좋아요 토글
    @PatchMapping("/{id}/like")
    public ResponseEntity<String> toggleLike(@PathVariable String id) {
        return historyRepository.findById(id)
                .map(history -> {
                    history.setLiked(!history.isLiked());
                    historyRepository.save(history);
                    return ResponseEntity.ok("Like status updated to " + history.isLiked());
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
