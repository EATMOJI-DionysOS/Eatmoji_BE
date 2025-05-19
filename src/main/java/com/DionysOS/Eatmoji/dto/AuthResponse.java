package com.DionysOS.Eatmoji.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private String email;
    private String message;
}
