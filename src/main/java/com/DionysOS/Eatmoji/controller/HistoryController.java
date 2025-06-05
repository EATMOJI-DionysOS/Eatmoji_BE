package com.DionysOS.Eatmoji.controller;

import com.DionysOS.Eatmoji.dto.FoodRecommend;
import com.DionysOS.Eatmoji.dto.RecommendResponse;
import com.DionysOS.Eatmoji.model.History;
import com.DionysOS.Eatmoji.repository.HistoryRepository;
import com.DionysOS.Eatmoji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    @PostMapping
    public ResponseEntity<History> saveHistory(@RequestBody History history) {
        history.setCreatedAt(LocalDateTime.now());
        history.setLiked(false);
        History saved = historyRepository.save(history);
        return ResponseEntity.ok(saved);
    }

    // 2. 이메일로 히스토리 조회
    @GetMapping
    public ResponseEntity<List<History>> getHistories() {
        return ResponseEntity.ok(historyRepository.findByEmail(userService.getCurrentUserEmail()));
    }

    // 3. 좋아요 토글
    @PatchMapping("/{id}/like")
    public ResponseEntity<String> toggleLike(@PathVariable String id, @RequestParam boolean like) {
        return historyRepository.findById(id)
                .map(history -> {
                    history.setLiked(like);
                    historyRepository.save(history);
                    return ResponseEntity.ok("Like status updated");
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public ResponseEntity<Void> saveHistory(@RequestBody RecommendResponse response) {

        try {
            for (FoodRecommend rec : response.getRecommendations()) {

                History history = new History(
                        userService.getCurrentUserEmail(),  // 사용자 인증 없을 경우 임시
                        response.getEmotion(),
                        rec.getFood(),
                        LocalDateTime.now(),
                        false
                );

                historyRepository.save(history);
            }
        } catch (Exception e) {
            System.err.println("❌ 저장 중 오류 발생:");
            e.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }

}
