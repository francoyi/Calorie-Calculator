package com.caloriecalc.entity;

public record LocalFoodItem(String name, double kcalPerServing) implements FoodItem {

    @Override
    public String getSource() {
        return "Manual";
    }
}
