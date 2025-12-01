package com.caloriecalc.usecase.myfoods.addmyfoodtomeal;

public class AddMyFoodToMealInputData {

    private final String foodName;
    private final double totalKcal;
    private final String unit;
    private final double amount;

    public AddMyFoodToMealInputData(String foodName, double totalKcal,double amount, String unit) {
        this.foodName = foodName;
        this.totalKcal = totalKcal;
        this.amount= amount;
        this.unit = unit;
    }

    public String getFoodName() { return foodName; }
    public double getTotalKcal() { return totalKcal; }
    public String getUnit() { return unit; }
    public double getAmount() { return amount; }
}