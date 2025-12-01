package com.caloriecalc.usecase.mealrecommendation;

import com.caloriecalc.entity.*;
import com.caloriecalc.entity.RecommenderFactory;
import com.caloriecalc.model.*;
import com.caloriecalc.usecase.searchfood.NutritionDataProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultMealRecommendationService extends MealRecommendationService {
    private final NutritionDataProvider nutritionProvider;
    private final RecommenderFactory recommenderFactory;

    public DefaultMealRecommendationService(NutritionDataProvider nutritionProvider, RecommenderFactory recommenderFactory) {
        this.nutritionProvider = nutritionProvider;
        this.recommenderFactory = recommenderFactory;
    }

    @Override
    public List<MealEntry> recommendMealEntries() {
        // 1. magic constant here
        List<String> foodNames = List.of(
                "apple",
                "banana",
                "steak",
                "salmon",
                "soybeans",
                "cereal",
                "cookie",
                "bread",
                "milk",
                "cheese",
                "egg",
                "chicken",
                "rice",
                "pasta",
                "potato",
                "carrot",
                "orange",
                "grape",
                "yogurt",
                "pizza",
                "tuna",
                "pork",
                "lamb",
                "tofu",
                "lentils"
        );

        // 2. Data fetching logic
        List<FoodItem> recommendedFoods = new ArrayList<>();
        for (String foodName : foodNames) {
            NutritionValues nv = nutritionProvider.fetchNutritionPer100(foodName);
            APIFoodItem apifi = new APIFoodItem(foodName, nv, 200);
            recommendedFoods.add(apifi);
        }

        // Recommender logic
        MealRecommender mr = (MealRecommender) recommenderFactory.create(recommendedFoods);
        List<Recommendation> lr = mr.getTopFoodRecommendations(1);

        if (lr.isEmpty()) {
            return Collections.emptyList(); // Handle alternative flow by returning an empty list
        }

        ArrayList<MealEntry> result = new ArrayList<>();

        for (FoodItem fi : lr.get(0).getFoodItems()) {
            APIFoodItem apiFoodItem = (APIFoodItem) fi;
            result.add(new MealEntry(apiFoodItem.name(), apiFoodItem.getSource(),
                    new Serving(apiFoodItem.servingAmount(), "g"), apiFoodItem.kcalPerServing(),
                    apiFoodItem.getSource(), null, null));
        }

        return result;
    }
}