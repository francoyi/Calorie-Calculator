package com.caloriecalc.port.history;
public interface VisualizeHistoryOutputBoundary {
    void present(VisualizeHistoryOutputData output);
    void prepareFailView(String error);
}