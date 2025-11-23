package com.caloriecalc.FoodItemImpl;

import com.caloriecalc.model.FoodItem;

public class FoodItemImpl implements FoodItem {
    private final String name;
    private final double kcalPerServing;

    public FoodItemImpl(String name, double kcalPerServing) {
        this.name = name;
        this.kcalPerServing = kcalPerServing;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public double kcalPerServing() {
        return kcalPerServing;
    }

    @Override
    public String getSource() {
        return "";
    }
}
