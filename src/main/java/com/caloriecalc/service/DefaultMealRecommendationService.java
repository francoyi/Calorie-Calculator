package com.caloriecalc.service;

import com.caloriecalc.factory.RecommenderFactory;
import com.caloriecalc.model.*;
import com.caloriecalc.port.NutritionDataProvider;

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
        // 1. TODO: magic constant here
        List<String> foodNames = List.of("apple", "banana", "steak", "salmon", "soybeans", "cereal", "cookie");

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

        return null;
    }
}