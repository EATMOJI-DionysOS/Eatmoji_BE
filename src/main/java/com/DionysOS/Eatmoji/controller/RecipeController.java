package com.DionysOS.Eatmoji.controller;

import com.DionysOS.Eatmoji.service.RecipeSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeController {

    private final RecipeSearchService recipeSearchService;

    @Autowired
    public RecipeController(RecipeSearchService recipeSearchService) {
        this.recipeSearchService = recipeSearchService;
    }

    @GetMapping("/api/recipe")
    public String getRecipe(@RequestParam String food) {
        String recipeNum = recipeSearchService.findRecipeByFood(food);
        return "https://www.10000recipe.com/recipe/" + recipeNum;
    }
}