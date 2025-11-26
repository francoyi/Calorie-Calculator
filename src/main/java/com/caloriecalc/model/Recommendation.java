package com.caloriecalc.model;

import java.util.ArrayList;
import java.util.List;

public class Recommendation {
    int calories;
    List<FoodItem> foodItems;

    public Recommendation(List<FoodItem> foodItems) {
        int sum = 0;
        this.foodItems = new ArrayList<>();
        if (!foodItems.isEmpty()) {
            for (int i = 0; i < foodItems.size(); i++) {
                this.foodItems.add(foodItems.get(i));
                sum += (int) foodItems.get(i).getKcalPerServing();
            }
        }
        this.calories = sum;
    }

    public void addFoodItem(FoodItem foodItem) {
        this.foodItems.add(foodItem);
        this.calories += (int) foodItem.getKcalPerServing();
    }

    public int getCalories() {
        return this.calories;
    }

    public List<FoodItem> getFoodItems() {
        return this.foodItems;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Calories: ").append(this.calories).append("\n");
        for (FoodItem foodItem : this.foodItems) {
            sb.append(foodItem.getName()).append(" ");
        }
        return sb.toString();
    }
}
