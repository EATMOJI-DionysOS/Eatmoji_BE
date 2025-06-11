package com.DionysOS.Eatmoji.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserProfileRequest {
    private List<String> category;
    private List<String> flavor;
    private List<String> disease;
    private List<String> allergy;
}
