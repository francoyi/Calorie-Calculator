package com.caloriecalc.port;

import com.caloriecalc.model.NutritionValues;

public interface NutritionDataProvider {
    NutritionValues fetchNutritionPer100(String term);
}
