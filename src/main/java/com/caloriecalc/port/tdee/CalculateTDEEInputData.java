package com.caloriecalc.port.tdee;

import com.caloriecalc.model.ActivityLevel;
import com.caloriecalc.model.UserMetrics;

public record CalculateTDEEInputData(
        int ageYears,
        double weight,
        double height,
        boolean metric,
        UserMetrics.Sex sex,
        ActivityLevel activityLevel
) { }
