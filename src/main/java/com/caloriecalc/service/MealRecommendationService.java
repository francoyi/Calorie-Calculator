package com.caloriecalc.service;

import com.caloriecalc.model.MealEntry;

import java.util.Collections;

import java.util.List;

public abstract class MealRecommendationService {
    public List<MealEntry> recommendMealEntries() {
        return Collections.emptyList();
    }
}
