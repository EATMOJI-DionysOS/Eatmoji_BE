package com.DionysOS.Eatmoji.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAuthRequest {
    private String newEmail;
    private String currentPassword; // to verify the user's identity
    private String newPassword;
}
