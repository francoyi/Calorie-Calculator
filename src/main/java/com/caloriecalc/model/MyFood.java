package com.caloriecalc.model;

import java.util.List;

/**
 * Domain entity representing a locally defined food (a recipe created by the user).
 *
 * This contains:
 *  - A user-defined food name
 *  - A list of ingredients (each with name, amount, unit, kcal)
 *  - A total kcal value derived from the ingredients
 *
 * This is a simple immutable entity used across the SaveToMyFood use case.
 */
public class MyFood {

    private final String name;
    private final List<Ingredient> ingredients;
    private final double totalKcal;

    public MyFood(String name, List<Ingredient> ingredients, double totalKcal) {
        this.name = name;
        this.ingredients = ingredients;
        this.totalKcal = totalKcal;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public double getTotalKcal() {
        return totalKcal;
    }
}

