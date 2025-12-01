package com.caloriecalc.port.tdee;

import com.caloriecalc.entity.NutritionValues;
import com.caloriecalc.usecase.searchfood.NutritionDataProvider;

public class NutritionDataProviderImpl implements NutritionDataProvider {

    @Override
    public NutritionValues fetchNutritionPer100(String term) {
        return new NutritionValues(100.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
    }
}
