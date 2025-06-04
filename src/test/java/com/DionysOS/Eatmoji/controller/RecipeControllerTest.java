package com.DionysOS.Eatmoji.controller;

import com.DionysOS.Eatmoji.service.RecipeSearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public RecipeSearchService recipeSearchService() {
            RecipeSearchService mockService = Mockito.mock(RecipeSearchService.class);
            when(mockService.findRecipeByFood("어묵김말이")).thenReturn("128671");
            return mockService;
        }
    }

    @Test
    @DisplayName("GET /api/recipe?food=어묵김말이 호출 시 올바른 링크 반환")
    void getRecipeTest() throws Exception {
        mockMvc.perform(get("/api/recipe").param("food", "어묵김말이"))
                .andExpect(status().isOk())
                .andExpect(content().string("https://www.10000recipe.com/recipe/128671"));
    }
}
