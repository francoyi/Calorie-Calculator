package com.caloriecalc.port.tdee;

public record CalculateTDEEOutputData(
        double bmr,
        double tdee,
        String formulaName,
        double activityFactor
) { }