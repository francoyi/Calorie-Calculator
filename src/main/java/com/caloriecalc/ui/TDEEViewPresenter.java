package com.caloriecalc.ui;

import com.caloriecalc.port.tdee.CalculateTDEEOutputBoundary;
import com.caloriecalc.port.tdee.CalculateTDEEOutputData;

import javax.swing.*;

public class TDEEViewPresenter implements CalculateTDEEOutputBoundary {

    private final TDEEView view;

    public TDEEViewPresenter(TDEEView view) {
        this.view = view;
    }

    @Override
    public void present(CalculateTDEEOutputData output) {
        SwingUtilities.invokeLater(() -> view.showResult(output));
    }

    @Override
    public void presentValidationError(String message) {
        SwingUtilities.invokeLater(() -> view.showValidationError(message));
    }
}