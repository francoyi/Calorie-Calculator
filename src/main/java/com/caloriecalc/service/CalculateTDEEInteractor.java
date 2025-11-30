package com.caloriecalc.service;

import com.caloriecalc.model.ActivityLevel;
import com.caloriecalc.model.CalDevianceRate;
import com.caloriecalc.model.UserMetrics;
import com.caloriecalc.port.tdee.*;

public class CalculateTDEEInteractor implements CalculateTDEEInputBoundary {
    private final BMRFormula bmrFormula;
    private final CalculateTDEEOutputBoundary presenter;

    public CalculateTDEEInteractor(BMRFormula bmrFormula, CalculateTDEEOutputBoundary presenter) {
        this.bmrFormula = bmrFormula;
        this.presenter = presenter;
    }

    @Override
    public void execute(CalculateTDEEInputData input) {
        try {

            final double weightKg = input.metric() ? input.weight() : input.weight() * 0.453592;
            final double heightCm = input.metric() ? input.height() : input.height() * 2.54;

            ActivityLevel lvl = input.activityLevel();
            CalDevianceRate rate = input.caldeviancerate();

            UserMetrics userMetrics = new UserMetrics(input.ageYears(), weightKg, heightCm, input.sex(), lvl, rate,
                    input.metric()
            );

            double userBMR = bmrFormula.computeBmr(userMetrics);
            userBMR = Math.max(0, userBMR);

            double userTDEE = userBMR * lvl.multiplier;
            userTDEE = userTDEE + rate.devianceRate;
            userTDEE = Math.max(0, userTDEE);

            presenter.present(new CalculateTDEEOutputData(userBMR, userTDEE, bmrFormula.name(), lvl.multiplier,
                    rate.devianceRate
            ));

        } catch (IllegalArgumentException ex) {
            presenter.presentValidationError(ex.getMessage());
        }
    }
}
