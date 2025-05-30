package com.DionysOS.Eatmoji.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

     // TODO: 현재 이 SecurityConfig는 모든 요청을 허용하고 있음. 나중에 인증 및 권한 설정을 추가해야 함.
     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         return http
                 .csrf(csrf -> csrf.disable()) // disable CSRF for API clients
                 .authorizeHttpRequests(auth -> auth
                         .anyRequest().permitAll()                     // allow all requests
                 )
                 .build();
     }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
