package com.caloriecalc.factory;

import java.util.List;

import com.caloriecalc.model.FoodItem;
import com.caloriecalc.model.MealRecommender;
import com.caloriecalc.model.Recommender;
import com.caloriecalc.service.FoodLogService;

public class MealRecommenderFactory implements RecommenderFactory {
    private final FoodLogService service;

    public MealRecommenderFactory(FoodLogService service) {
        this.service = service;
    }

    @Override
    public Recommender create(List<FoodItem> foods) {
        double goal = service.getDailyGoal();
        return new MealRecommender(foods, goal);
    }
}