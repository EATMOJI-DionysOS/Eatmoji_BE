package com.DionysOS.Eatmoji.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserProfileResponse {
    private String email;
    private List<String> category;
    private List<String> flavor;
    private List<String> disease;
    private List<String> allergy;
}
