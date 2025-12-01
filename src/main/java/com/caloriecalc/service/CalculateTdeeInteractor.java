package com.caloriecalc.service;

import com.caloriecalc.model.ActivityLevel;
import com.caloriecalc.model.CalDevianceRate;
import com.caloriecalc.model.UserMetrics;
import com.caloriecalc.port.tdee.BmrFormula;
import com.caloriecalc.port.tdee.CalculateTdeeInputBoundary;
import com.caloriecalc.port.tdee.CalculateTdeeInputData;
import com.caloriecalc.port.tdee.CalculateTdeeOutputBoundary;
import com.caloriecalc.port.tdee.CalculateTdeeOutputData;

public class CalculateTdeeInteractor implements CalculateTdeeInputBoundary {
    private final BmrFormula bmrFormula;
    private final CalculateTdeeOutputBoundary presenter;
    double inToCm = 0.453592;
    double kgToLbs = 2.54;

    public CalculateTdeeInteractor(BmrFormula bmrFormula, CalculateTdeeOutputBoundary presenter) {
        this.bmrFormula = bmrFormula;
        this.presenter = presenter;
    }

    @Override
    public void execute(CalculateTdeeInputData input) {
        try {
            final double weightKg;
            final double heightCm;

            if (input.metric()) {
                weightKg = input.weight();
                heightCm = input.height();
            }
            else {
                weightKg = input.weight() * inToCm;
                heightCm = input.height() * kgToLbs;
            }

            ActivityLevel lvl = input.activityLevel();
            final CalDevianceRate rate = input.caldeviancerate();

            final UserMetrics userMetrics = new UserMetrics(input.ageYears(), weightKg, heightCm, input.sex(),
                    lvl, rate, input.metric()
            );

            double userBmr = bmrFormula.computeBmr(userMetrics);
            userBmr = Math.max(0, userBmr);

            double userTdee = userBmr * lvl.multiplier;
            userTdee = userTdee + rate.devianceRate;
            userTdee = Math.max(0, userTdee);

            presenter.present(new CalculateTdeeOutputData(userBmr, userTdee, bmrFormula.name(), lvl.multiplier,
                    rate.devianceRate
            ));

        }
        catch (IllegalArgumentException ex) {
            presenter.presentValidationError(ex.getMessage());
        }
    }
}
