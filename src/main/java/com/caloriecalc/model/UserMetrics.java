package com.caloriecalc.model;

public record UserMetrics(int ageYears, double weightKg, double heightCm, Sex sex) {
    public enum Sex {MALE, FEMALE}

    public UserMetrics {
        if (ageYears <= 17) throw new IllegalArgumentException("Age must be at least 18.");
        if (weightKg <= 0) throw new IllegalArgumentException("Weight must be positive.");
        if (heightCm <= 0) throw new IllegalArgumentException("Height must be positive.");
    }
}