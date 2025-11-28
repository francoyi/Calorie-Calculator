package com.caloriecalc.ui;
import com.caloriecalc.port.history.*;

public class VisualizeHistoryController {
    private final VisualizeHistoryInputBoundary interactor;

    public VisualizeHistoryController(VisualizeHistoryInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        interactor.execute(new VisualizeHistoryInputData());
    }
}