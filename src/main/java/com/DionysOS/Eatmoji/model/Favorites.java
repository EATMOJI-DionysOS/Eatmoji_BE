package com.DionysOS.Eatmoji.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "favorites")
public class Favorites {

    // controller에서 해당 조건들에 만족하는 유효성 검사 필요

    @Email
    @NotBlank
    private String email;

    @NotNull
    private String favorite;

    public Favorites(String email, String favorite) {
        this.email = email;
        this.favorite = favorite;
    }
}