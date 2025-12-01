package com.caloriecalc.usecase.tdee;

public interface CalculateTDEEOutputBoundary {
    void present(CalculateTDEEOutputData output);

    void presentValidationError(String message);
}