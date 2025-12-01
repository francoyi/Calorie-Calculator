package com.caloriecalc.usecase.history;
public interface VisualizeHistoryOutputBoundary {
    void present(VisualizeHistoryOutputData output);
    void prepareFailView(String error);
}