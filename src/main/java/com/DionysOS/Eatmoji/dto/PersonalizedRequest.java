package com.DionysOS.Eatmoji.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
// 내부에서 임시로 만드는 데이터 전송용 VO 같은 느낌
public class PersonalizedRequest {
    private String email;
    private List<String> category;
    private List<String> flavor;
    private List<String> disease;
    private List<String> allergy;
    private List<String> likedFoods;
}

