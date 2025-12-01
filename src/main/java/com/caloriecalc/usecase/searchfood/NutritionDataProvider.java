package com.caloriecalc.usecase.searchfood;

import com.caloriecalc.entity.NutritionValues;

public interface NutritionDataProvider {
    NutritionValues fetchNutritionPer100(String term);

}
