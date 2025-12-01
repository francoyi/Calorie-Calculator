package com.caloriecalc.usecase.tdee;

import com.caloriecalc.entity.ActivityLevel;
import com.caloriecalc.entity.CalDevianceRate;
import com.caloriecalc.entity.UserMetrics;

public record CalculateTDEEInputData(
        int ageYears,
        double weight,
        double height,
        boolean metric,
        UserMetrics.Sex sex,
        ActivityLevel activityLevel,
        CalDevianceRate caldeviancerate
) {
}
