package com.caloriecalc.model;

public final class UserMetrics {
    public enum Sex { MALE, FEMALE }

    private final int ageYears;
    private final double weightKg;
    private final double heightCm;
    private final Sex sex;

    public UserMetrics(int ageYears, double weightKg, double heightCm, Sex sex) {
        if (ageYears <= 17) throw new IllegalArgumentException("Age must be at least 18.");
        if (weightKg <= 0) throw new IllegalArgumentException("Weight must be positive.");
        if (heightCm <= 0) throw new IllegalArgumentException("Height must be positive.");
        this.ageYears = ageYears;
        this.weightKg = weightKg;
        this.heightCm = heightCm;
        this.sex = sex;
    }

    public int ageYears() { return ageYears; }
    public double weightKg() { return weightKg; }
    public double heightCm() { return heightCm; }
    public Sex sex() { return sex; }
}