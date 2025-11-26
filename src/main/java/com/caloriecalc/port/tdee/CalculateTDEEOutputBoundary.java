package com.caloriecalc.port.tdee;

public interface CalculateTDEEOutputBoundary {
    void present(CalculateTDEEOutputData output);
    void presentValidationError(String message);
}