package com.caloriecalc.usecase.tdee;

public interface CalculateTdeeInputBoundary {
    /**
     * Executes the Calculate-TDEE use case using the provided input data.
     *
     * @param input request model containing user metrics and calculation parameters;
     *              must not be {@code null}
     */
    void execute(CalculateTdeeInputData input);
}
