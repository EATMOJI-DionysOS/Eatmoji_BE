package com.DionysOS.Eatmoji.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Getter
@Document(collection = "histories")
public class History {

    // controller에서 해당 조건들에 만족하는 유효성 검사 필요

    @Email
    @NotBlank
    private String email;

    @NotNull
    private String emotion;

    @NotNull
    private String food;

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    public History(String email, String emotion, String food, LocalDateTime createdAt) {
        this.email = email;
        this.emotion = emotion;
        this.food = food;
        this.createdAt = createdAt;
    }


}