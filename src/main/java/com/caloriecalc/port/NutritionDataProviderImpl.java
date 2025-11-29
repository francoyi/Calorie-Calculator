package com.caloriecalc.port;

import com.caloriecalc.model.NutritionValues;

public class NutritionDataProviderImpl implements NutritionDataProvider {

    public NutritionValues fetchNutritionPer100(String term) {
        String lowerTerm = term.toLowerCase();
        // Double energyKcal, Double proteinG, Double fatG, Double carbG, Double sugarG, Double fiberG, Double sodiumMg
        return switch (lowerTerm) {
            case "apple" -> new NutritionValues(52.0, 0.3, 0.2, 13.8, 10.4, 2.4, 1.0);
            case "banana" -> new NutritionValues(89.0, 1.1, 0.3, 22.8, 12.2, 2.6, 1.0);
            case "steak" -> new NutritionValues(271.0, 29.8, 15.6, 0.0, 0.0, 0.0, 75.0);
            case "salmon" -> new NutritionValues(208.0, 20.4, 13.4, 0.0, 0.0, 0.0, 59.0);
            case "soybeans" -> new NutritionValues(173.0, 16.6, 9.0, 9.9, 3.0, 6.0, 5.0);
            case "cereal" -> new NutritionValues(379.0, 8.0, 2.0, 84.0, 25.0, 3.0, 550.0);
            case "cookie" -> new NutritionValues(480.0, 5.0, 22.0, 67.0, 35.0, 2.0, 300.0);
            case "bread" -> new NutritionValues(266.0, 7.9, 3.4, 49.0, 5.5, 2.7, 491.0);
            case "milk" -> new NutritionValues(50.0, 3.3, 2.0, 4.8, 4.8, 0.0, 44.0);
            case "cheese" -> new NutritionValues(403.0, 24.9, 33.1, 1.3, 0.5, 0.0, 621.0);
            case "egg" -> new NutritionValues(155.0, 12.6, 10.6, 1.1, 1.1, 0.0, 124.0);
            case "chicken" -> new NutritionValues(165.0, 31.0, 3.6, 0.0, 0.0, 0.0, 74.0);
            case "rice" -> new NutritionValues(130.0, 2.7, 0.3, 28.2, 0.1, 0.4, 1.0);
            case "pasta" -> new NutritionValues(158.0, 5.8, 0.9, 30.6, 0.6, 1.8, 6.0);
            case "potato" -> new NutritionValues(93.0, 2.5, 0.1, 21.3, 0.7, 2.2, 6.0);
            case "carrot" -> new NutritionValues(41.0, 0.9, 0.2, 9.6, 4.7, 2.8, 69.0);
            case "orange" -> new NutritionValues(47.0, 0.9, 0.1, 11.8, 9.4, 2.4, 0.0);
            case "grape" -> new NutritionValues(69.0, 0.7, 0.2, 18.1, 15.5, 0.9, 2.0);
            case "yogurt" -> new NutritionValues(61.0, 5.3, 1.6, 7.0, 7.0, 0.0, 65.0);
            case "pizza" -> new NutritionValues(267.0, 12.0, 11.0, 30.0, 3.0, 2.0, 580.0);
            case "tuna" -> new NutritionValues(132.0, 28.0, 1.0, 0.0, 0.0, 0.0, 45.0);
            case "pork" -> new NutritionValues(242.0, 27.0, 14.0, 0.0, 0.0, 0.0, 62.0);
            case "lamb" -> new NutritionValues(250.0, 25.0, 16.9, 0.0, 0.0, 0.0, 80.0);
            case "tofu" -> new NutritionValues(76.0, 8.0, 4.8, 1.9, 0.4, 0.9, 7.0);
            case "lentils" -> new NutritionValues(116.0, 9.0, 0.4, 20.1, 1.8, 7.9, 2.0);
            default -> new NutritionValues(100.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        };
    }
}
