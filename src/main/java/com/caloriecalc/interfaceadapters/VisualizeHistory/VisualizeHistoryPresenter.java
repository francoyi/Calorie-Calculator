package com.caloriecalc.interfaceadapters.VisualizeHistory;
import com.caloriecalc.ui.NutritionHistoryPanel;
import com.caloriecalc.usecase.history.VisualizeHistoryOutputBoundary;
import com.caloriecalc.usecase.history.VisualizeHistoryOutputData;

import javax.swing.*;
import java.awt.*;

public class VisualizeHistoryPresenter implements VisualizeHistoryOutputBoundary {
    private final Component parent;

    public VisualizeHistoryPresenter(Component parent) {
        this.parent = parent;
    }

    @Override
    public void present(VisualizeHistoryOutputData output) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), "Calorie History", Dialog.ModalityType.APPLICATION_MODAL);
        NutritionHistoryPanel panel = new NutritionHistoryPanel();
        panel.updateData(output.caloriesPerDay(), output.calorieGoal());

        dialog.add(panel);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    @Override
    public void prepareFailView(String error) {
        JOptionPane.showMessageDialog(parent, error, "History Error", JOptionPane.INFORMATION_MESSAGE);
    }
}