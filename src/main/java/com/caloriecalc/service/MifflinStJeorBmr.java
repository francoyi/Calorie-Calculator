package com.caloriecalc.service;

import com.caloriecalc.model.UserMetrics;
import com.caloriecalc.port.tdee.BmrFormula;

// Mifflin–St Jeor BMR: BMR = 10*kg + 6.25*cm − 5*age + s, where s = +5 m or −161 f.
public class MifflinStJeorBmr implements BmrFormula {
    @Override
    public String name() {
        return "Mifflin-St Jeor";
    }

    @Override
    public double computeBmr(UserMetrics userMetrics) {
        double s = -161.0;
        if (userMetrics.sex() == UserMetrics.Sex.MALE) {
            s = 5.0;
        }
        return 10.0 * userMetrics.weightKg() + 6.25 * userMetrics.heightCm() - 5.0 * userMetrics.ageYears() + s;
    }
}
