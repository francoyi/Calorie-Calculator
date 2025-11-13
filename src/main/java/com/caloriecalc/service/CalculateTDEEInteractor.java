package com.caloriecalc.service;

import com.caloriecalc.port.tdee.BMRFormula;
import com.caloriecalc.port.tdee.CalculateTDEEInputBoundary;
import com.caloriecalc.port.tdee.CalculateTDEEInputData;
import com.caloriecalc.port.tdee.CalculateTDEEOutputBoundary;

public class CalculateTDEEInteractor implements CalculateTDEEInputBoundary {
    private final BMRFormula bmrFormula;
    private final CalculateTDEEOutputBoundary presenter;

    public CalculateTDEEInteractor(BMRFormula bmrFormula, CalculateTDEEOutputBoundary presenter) {
        this.bmrFormula = bmrFormula;
        this.presenter = presenter;
    }

    @Override
    public void execute(CalculateTDEEInputData input) {

    }
}
