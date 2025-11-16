package com.caloriecalc.model;

public class Ingredient {
    private final String name;
    private final double amount;
    private final String unit;
    private final Double kcal;

    public Ingredient(String name, double amount, String unit, Double kcal) {
        this.name = name;
        this.amount =amount;
        this.unit = unit;
        this.kcal = kcal;
    }

    public String getName() {return name; }
    public double getAmount() {return amount;}
    public String getunit() {return unit;}
    public Double getKcal() {return kcal;}
}
