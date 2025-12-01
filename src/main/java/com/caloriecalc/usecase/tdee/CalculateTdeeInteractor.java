package com.caloriecalc.usecase.tdee;

import com.caloriecalc.entity.ActivityLevel;
import com.caloriecalc.entity.CalDevianceRate;
import com.caloriecalc.entity.UserMetrics;

public class CalculateTdeeInteractor implements CalculateTdeeInputBoundary {

    private static final double LB_TO_KG = 0.453592;
    private static final double IN_TO_CM = 2.54;

    private final BmrFormula bmrFormula;
    private final CalculateTdeeOutputBoundary presenter;

    public CalculateTdeeInteractor(BmrFormula bmrFormula, CalculateTdeeOutputBoundary presenter) {
        this.bmrFormula = bmrFormula;
        this.presenter = presenter;
    }

    @Override
    public void execute(CalculateTdeeInputData input) {
        try {
            final boolean metric = input.metric();

            final double weightKg;
            if (metric) {
                weightKg = input.weight();
            }
            else {
                weightKg = input.weight() * LB_TO_KG;
            }

            final double heightCm;
            if (metric) {
                heightCm = input.height();
            }
            else {
                heightCm = input.height() * IN_TO_CM;
            }

            final ActivityLevel lvl = input.activityLevel();
            final CalDevianceRate rate = input.caldeviancerate();

            final UserMetrics userMetrics = new UserMetrics(
                    input.ageYears(),
                    weightKg,
                    heightCm,
                    input.sex(),
                    lvl,
                    rate,
                    metric
            );

            double userBmr = bmrFormula.computeBmr(userMetrics);
            userBmr = Math.max(0, userBmr);

            double userTdee = userBmr * lvl.multiplier;
            userTdee = userTdee + rate.devianceRate;
            userTdee = Math.max(0, userTdee);

            presenter.present(new CalculateTdeeOutputData(
                    userBmr,
                    userTdee,
                    bmrFormula.name(),
                    lvl.multiplier,
                    rate.devianceRate
            ));
        }
        catch (IllegalArgumentException ex) {
            presenter.presentValidationError(ex.getMessage());
        }
    }
}
