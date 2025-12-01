package com.caloriecalc.port.tdee;

import com.caloriecalc.model.ActivityLevel;
import com.caloriecalc.model.CalDevianceRate;
import com.caloriecalc.model.UserMetrics;

public record CalculateTdeeInputData(
        int ageYears,
        double weight,
        double height,
        boolean metric,
        UserMetrics.Sex sex,
        ActivityLevel activityLevel,
        CalDevianceRate caldeviancerate
) {
}
