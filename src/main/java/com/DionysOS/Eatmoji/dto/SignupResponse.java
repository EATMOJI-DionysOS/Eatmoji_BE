package com.DionysOS.Eatmoji.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupResponse {
    private String email;
    private String message;
}
