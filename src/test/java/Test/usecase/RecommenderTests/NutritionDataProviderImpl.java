package Test.usecase.RecommenderTests;

import com.caloriecalc.model.NutritionValues;
import com.caloriecalc.port.NutritionDataProvider;

public class NutritionDataProviderImpl implements NutritionDataProvider {

    @Override
    public NutritionValues fetchNutritionPer100(String term) {
        return new NutritionValues(100.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
    }
}
