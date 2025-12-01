package com.caloriecalc.interfaceadapters.TDEE;

import com.caloriecalc.ui.TDEEView;
import com.caloriecalc.usecase.tdee.CalculateTDEEOutputBoundary;
import com.caloriecalc.usecase.tdee.CalculateTDEEOutputData;

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