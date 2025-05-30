package com.DionysOS.Eatmoji.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String email;
    private String message;
}
