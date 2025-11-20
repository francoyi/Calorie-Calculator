package com.caloriecalc.service;

import com.caloriecalc.model.ActivityLevel;
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

            if (input.ageYears() < 18) {
                presenter.presentValidationError("Age must be 18 or above.");
                return;
            }
            if (input.weight() <= 0) {
                presenter.presentValidationError("Weight must be greater than 0.");
                return;
            }
            if (input.height() <= 0) {
                presenter.presentValidationError("Height must be greater than 0.");
                return;
            }


            final double weightKg = input.metric() ? input.weight() : input.weight() * 0.453592;
            final double heightCm = input.metric() ? input.height() : input.height() * 2.54;

            UserMetrics userMetrics = new UserMetrics(input.ageYears(), weightKg, heightCm, input.sex());


            double userBMR = bmrFormula.computeBmr(userMetrics);


            userBMR = Math.max(0, userBMR);


            ActivityLevel lvl = input.activityLevel();
            double userTDEE = userBMR * lvl.multiplier;


            userTDEE = Math.max(0, userTDEE);


            presenter.present(new CalculateTDEEOutputData(
                    userBMR,
                    userTDEE,
                    bmrFormula.name(),
                    lvl.multiplier
            ));

        } catch (IllegalArgumentException ex) {
            presenter.presentValidationError(ex.getMessage());
        }
    }
}
