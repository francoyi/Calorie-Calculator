package com.caloriecalc.port;

import com.caloriecalc.model.UserMetrics;

public interface BMRFormula {
    String name();
    double computeBmr(UserMetrics a);
}
