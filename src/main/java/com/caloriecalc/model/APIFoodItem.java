package com.caloriecalc.model;

public record APIFoodItem(String name, NutritionValues per100g, double servingAmount) implements FoodItem {

    @Override
    public double kcalPerServing() {
        if (per100g == null || per100g.energyKcal() == null) return Double.NaN;
        return per100g.energyKcal() * (servingAmount / 100.0);
    }

    @Override
    public String getSource() {
        return "OpenFoodFacts";
    }
}
