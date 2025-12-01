package com.caloriecalc.usecase.tdee;

public record CalculateTDEEOutputData(
        double bmr,
        double tdee,
        String formulaName,
        double activityFactor,
        double calDeviance
) {
}