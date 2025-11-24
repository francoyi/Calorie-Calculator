package com.caloriecalc.port.tdee;

import com.caloriecalc.model.UserMetrics;

public interface BMRFormula {
    String name();

    double computeBmr(UserMetrics a);
}
