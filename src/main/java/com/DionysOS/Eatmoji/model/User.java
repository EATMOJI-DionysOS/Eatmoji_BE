package com.DionysOS.Eatmoji.model;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
public class User {

    @Id
    @Email
    @NotBlank
    @Indexed(unique = true)
    @Getter
    private String email;  // user 생성 시에 controller에서 중복 검증 필요

    @NotNull
    @Getter
    private String password;

    @Setter
    private List<String> category = new ArrayList<>(); // 선호하는 음식 종류 (한식 / 중식 / 일식 / 양식 등)

    @Setter
    private List<String> flavor = new ArrayList<>(); // 선호하는 맛 (매운맛 / 단맛 등)

    @Setter
    private List<String> disease = new ArrayList<>(); // 질병 정보 (고혈압 / 당뇨 등)

    @Setter
    private List<String> allergy = new ArrayList<>(); // 알레르기 정보 (우유 / 계란 등)

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public List<String> getCategory() {
        if (category == null) category = new ArrayList<>();
        return category;
    }

    public List<String> getFlavor() {
        if (flavor == null) flavor = new ArrayList<>();
        return flavor;
    }

    public List<String> getDisease() {
        if (disease == null) disease = new ArrayList<>();
        return disease;
    }

    public List<String> getAllergy() {
        if (allergy == null) allergy = new ArrayList<>();
        return allergy;
    }

}