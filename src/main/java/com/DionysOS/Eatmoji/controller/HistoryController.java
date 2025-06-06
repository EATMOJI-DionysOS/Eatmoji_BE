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

    // 1. 히스토리 저장 (추천 받았을 때)
    // TODO: 테스트용임. 이걸로 실제로 히스토리를 저장하지는 않을 것 (/save에서 저장할 것), 나중에 지워야 함!
    // TODO: 디버깅용임 (/history 에서 POST 요청 보내면 다이렉트로 히스토리 저장되게 함)
//    @PostMapping
//    public ResponseEntity<History> saveHistory(@RequestBody History history) {
//        history.setCreatedAt(LocalDateTime.now());
//        history.setLiked(false);
//        History saved = historyRepository.save(history);
//        return ResponseEntity.ok(saved);
//    }

    // 2. 이메일로 히스토리 조회
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

    // 3. 좋아요 토글
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

    @PostMapping("/save")
    public ResponseEntity<List<HistoryResponse>> saveHistory(@RequestBody RecommendResponse response) {
        List<HistoryResponse> responseList = new ArrayList<>();

        try {
            for (FoodRecommend rec : response.getRecommendations()) {
                History history = new History(
                        userService.getCurrentUserEmail(),
                        response.getEmotion(),
                        rec.getFood(),
                        rec.getReason(),
                        LocalDateTime.now(),
                        false
                );

                History saved = historyRepository.save(history);

                // DTO로 변환
                HistoryResponse dto = new HistoryResponse();
                dto.setId(saved.getId());
                dto.setEmail(saved.getEmail());
                dto.setEmotion(saved.getEmotion());
                dto.setCreatedAt(saved.getCreatedAt());
                dto.setLiked(saved.isLiked());

                FoodRecommend dtoRec = new FoodRecommend();
                dtoRec.setFood(saved.getFood());
                dtoRec.setReason(saved.getReason());
                dto.setRecommendation(dtoRec);

                responseList.add(dto);
            }
        } catch (Exception e) {
            System.err.println("❌ 저장 중 오류 발생:");
            e.printStackTrace();
        }

        return ResponseEntity.ok(responseList);
    }
}
