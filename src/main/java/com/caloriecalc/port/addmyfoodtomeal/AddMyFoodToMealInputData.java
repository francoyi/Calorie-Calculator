package com.caloriecalc.port.addmyfoodtomeal;

public class AddMyFoodToMealInputData {

    private final String foodName;
    private final double totalKcal;

    public AddMyFoodToMealInputData(String foodName, double totalKcal) {
        this.foodName = foodName;
        this.totalKcal = totalKcal;
    }

    public String getFoodName() { return foodName; }
    public double getTotalKcal() { return totalKcal; }
}
