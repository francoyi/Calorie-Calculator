package com.caloriecalc.entity;

import java.util.List;

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