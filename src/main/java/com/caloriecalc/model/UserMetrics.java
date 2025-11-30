package com.caloriecalc.model;

public record UserMetrics(
        int ageYears,
        double weightKg,
        double heightCm,
        Sex sex,
        ActivityLevel activityLevel,
        CalDevianceRate calDevianceRate,
        boolean metricInput
) {
    public enum Sex {MALE, FEMALE}

    public UserMetrics {
        if (ageYears <= 17) throw new IllegalArgumentException("Age must be 18 or above.");
        if (weightKg <= 0) throw new IllegalArgumentException("Weight must be greater than 0.");
        if (heightCm <= 0) throw new IllegalArgumentException("Height must be greater than 0.");
        if (activityLevel == null) throw new IllegalArgumentException("Activity level must be provided.");
        if (calDevianceRate == null) throw new IllegalArgumentException("Calorie deviance rate must be provided.");
    }
}
