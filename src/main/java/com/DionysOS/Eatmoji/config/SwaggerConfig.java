package com.DionysOS.Eatmoji.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // OpenAPI 문서의 기본 정보
        Info info = new Info()
                .title("Eatmoji API 문서")
                .description("이모지 기반 및 개인화 추천 API를 제공합니다.")
                .version("1.0.0");

        // JWT 인증 스키마
        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        // 보안 설정
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("BearerAuth");

        // 최종 OpenAPI 빌드
        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(new Components().addSecuritySchemes("BearerAuth", securityScheme));
    }
}
