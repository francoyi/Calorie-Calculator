package com.caloriecalc.model;

import java.util.List;

public interface Recommender {
    List<Recommendation> getTopFoodRecommendations(int count);
}
