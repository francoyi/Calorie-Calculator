package com.caloriecalc.port.visualize;

public record VisualizeNutritionOutputData(
        double totalProtein,
        double totalFat,
        double totalCarbs,
        double totalCalories
) {
}