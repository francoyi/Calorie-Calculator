package com.caloriecalc.ui;

import com.caloriecalc.port.visualize.VisualizeNutritionOutputBoundary;
import com.caloriecalc.port.visualize.VisualizeNutritionOutputData;
import javax.swing.*;
import java.awt.*;

public class VisualizeNutritionPresenter implements VisualizeNutritionOutputBoundary {
    private final Component parent;

    public VisualizeNutritionPresenter(Component parent) {
        this.parent = parent;
    }

    @Override
    public void present(VisualizeNutritionOutputData output) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), "Nutrition Breakdown", Dialog.ModalityType.APPLICATION_MODAL);

        NutritionPieChartPanel chart = new NutritionPieChartPanel();
        chart.updateData(output.totalProtein(), output.totalFat(), output.totalCarbs());

        dialog.add(chart);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    @Override
    public void prepareFailView(String error) {
        JOptionPane.showMessageDialog(parent, error, "Error", JOptionPane.ERROR_MESSAGE);
    }
}