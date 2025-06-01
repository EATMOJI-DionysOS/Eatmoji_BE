package com.DionysOS.Eatmoji.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileRequest {
    private AttributeUpdateRequest category;
    private AttributeUpdateRequest flavor;
    private AttributeUpdateRequest disease;
    private AttributeUpdateRequest allergy;
}
