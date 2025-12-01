package com.caloriecalc.usecase.tdee;

public record CalculateTdeeOutputData(
        double bmr,
        double tdee,
        String formulaName,
        double activityFactor,
        double calDeviance
) {
}
