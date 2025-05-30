package com.DionysOS.Eatmoji.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponse {
    private String email;
    private String category;
    private String flavor;
    private String disease;
    private String allergy;
}
