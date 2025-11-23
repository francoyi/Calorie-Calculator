package com.caloriecalc.model;

import com.caloriecalc.FoodItemImpl.FoodItemImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MealRecommenderTest {

    @Test
    void GetTop10Test() {
        FoodItemImpl a = new FoodItemImpl("a", 700);
        FoodItemImpl b = new FoodItemImpl("b", 1400);
        FoodItemImpl c = new FoodItemImpl("c", 200);

        List<FoodItem> foodItems = new ArrayList<>();
        foodItems.add(a);
        foodItems.add(b);
        foodItems.add(c);

        boolean flag = true;

        MealRecommender fr = new MealRecommender(foodItems, 2900);
        List<Recommendation> top = fr.getTopFoodRecommendations(10);
        for (Recommendation recommendation : top) {
            if (recommendation.getCalories() > fr.getGoal()) {
                flag = false;
                break;
            }
        }

        assertTrue(flag, "Each sum should have kcal sum lesser than goal");
        assertTrue(top.size() < 11, "Should provide at most 10 recommendations");
    }
}