package com.caloriecalc.usecase.tdee;

import com.caloriecalc.entity.UserMetrics;

//Raw Output - BMR formula is ALLOWED to output negative numbers!!! Anything using this must handle it as such.

public interface BMRFormula {
    String name();

    double computeBmr(UserMetrics a);
}
