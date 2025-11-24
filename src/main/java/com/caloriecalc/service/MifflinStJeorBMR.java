package com.caloriecalc.service;

import com.caloriecalc.model.UserMetrics;
import com.caloriecalc.port.tdee.BMRFormula;

// Mifflin–St Jeor BMR: BMR = 10*kg + 6.25*cm − 5*age + s, where s = +5 m or −161 f.
public class MifflinStJeorBMR implements BMRFormula {
    @Override
    public String name() {
        return "Mifflin–St Jeor";
    }

    @Override
    public double computeBmr(UserMetrics a) {
        double s = -161.0;
        if (a.sex() == UserMetrics.Sex.MALE) {
            s = 5.0;
        }
        return 10.0 * a.weightKg() + 6.25 * a.heightCm() - 5.0 * a.ageYears() + s;
    }
}
