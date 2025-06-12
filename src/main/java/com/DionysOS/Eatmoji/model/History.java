package com.DionysOS.Eatmoji.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "histories")
public class History {
    @Id
    private String id;


    // controller에서 해당 조건들에 만족하는 유효성 검사 필요

    @Email
    @NotBlank
    private String email;

    @NotNull
    private String emotion;

    @NotNull
    private String food;

    @NotNull
    private String reason;

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    private boolean isLiked = false;

    public History(String email, String emotion, String food, String reason, LocalDateTime createdAt, boolean isLiked) {
        this.email = email;
        this.emotion = emotion;
        this.food = food;
        this.reason = reason;
        this.createdAt = createdAt;
        this.isLiked = isLiked;
    }


}