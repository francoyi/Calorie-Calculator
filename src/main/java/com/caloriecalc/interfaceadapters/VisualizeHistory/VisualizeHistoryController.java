package com.caloriecalc.interfaceadapters.VisualizeHistory;
import com.caloriecalc.usecase.history.VisualizeHistoryInputBoundary;
import com.caloriecalc.usecase.history.VisualizeHistoryInputData;

public class VisualizeHistoryController {
    private final VisualizeHistoryInputBoundary interactor;

    public VisualizeHistoryController(VisualizeHistoryInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        interactor.execute(new VisualizeHistoryInputData());
    }
}