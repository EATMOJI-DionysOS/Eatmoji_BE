package com.DionysOS.Eatmoji.model;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Getter
@Document(collection = "users")
public class User {

    @Id
    @Email
    @NotBlank
    @Indexed(unique = true)
    private String email;  // user 생성 시에 controller에서 중복 검증 필요

    @NotNull
    private String password;

    @Setter
    private String category;

    @Setter
    private String flavor;

    @Setter
    private String disease;

    @Setter
    private String allergy;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


}