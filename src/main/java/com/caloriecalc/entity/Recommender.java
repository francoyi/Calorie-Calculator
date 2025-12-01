package com.caloriecalc.entity;

import java.util.List;

public interface Recommender {
    List<Recommendation> getTopFoodRecommendations(int count);
}
