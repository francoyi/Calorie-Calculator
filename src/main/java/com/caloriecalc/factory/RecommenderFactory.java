package com.caloriecalc.factory;

import com.caloriecalc.model.FoodItem;
import com.caloriecalc.model.Recommender;

import java.util.List;

public interface RecommenderFactory {
    /**
     * Creates a new Recommender instance initialized with the specific foods
     * accounting for the user's daily goal.
     */
    Recommender create(List<FoodItem> foods);
}
