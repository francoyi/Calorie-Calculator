package com.caloriecalc.port.tdee;

public record CalculateTdeeOutputData(
        double bmr,
        double tdee,
        String formulaName,
        double activityFactor,
        double calDeviance
) {
}
