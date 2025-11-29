package com.caloriecalc.factory;

import java.util.List;

import com.caloriecalc.model.FoodItem;
import com.caloriecalc.model.GoalProvider;
import com.caloriecalc.model.MealRecommender;
import com.caloriecalc.model.Recommender;
import com.caloriecalc.service.FoodLogService;

public class MealRecommenderFactory implements RecommenderFactory {
    private final GoalProvider goalProvider;

    public MealRecommenderFactory(GoalProvider goalProvider) {
        this.goalProvider = goalProvider;
    }

    @Override
    public Recommender create(List<FoodItem> foods) {
        double goal = goalProvider.getDailyGoal();
        return new MealRecommender(foods, goal);
    }
}