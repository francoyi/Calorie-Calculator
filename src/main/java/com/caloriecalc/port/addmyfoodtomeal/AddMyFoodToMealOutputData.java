package com.caloriecalc.port.addmyfoodtomeal;

public class AddMyFoodToMealOutputData {

    private final String foodName;
    private final double amount;
    private final String unit;
    private final double totalKcal;

    public AddMyFoodToMealOutputData(String foodName, double amount, String unit, double totalKcal) {
        this.foodName = foodName;
        this.amount = amount;
        this.unit = unit;
        this.totalKcal = totalKcal;
    }

    public String getFoodName() { return foodName; }
    public double getAmount() { return amount; }
    public String getUnit() { return unit; }
    public double getTotalKcal() { return totalKcal; }
}