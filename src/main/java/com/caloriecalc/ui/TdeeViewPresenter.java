package com.caloriecalc.ui;

import com.caloriecalc.port.tdee.CalculateTdeeOutputBoundary;
import com.caloriecalc.port.tdee.CalculateTdeeOutputData;

import javax.swing.*;

public class TdeeViewPresenter implements CalculateTdeeOutputBoundary {

    private final TDEEView view;

    public TdeeViewPresenter(TDEEView view) {
        this.view = view;
    }

    @Override
    public void present(CalculateTdeeOutputData output) {
        SwingUtilities.invokeLater(() -> view.showResult(output));
    }

    @Override
    public void presentValidationError(String message) {
        SwingUtilities.invokeLater(() -> view.showValidationError(message));
    }
}