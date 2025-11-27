package com.caloriecalc.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;

/**
 * A custom Swing component that renders a Pie Chart for macronutrients.
 * <p>
 * This panel overrides {@code paintComponent} to draw arcs representing
 * Protein, Fat, and Carbs using Java 2D Graphics.
 * </p>
 */
public class NutritionPieChartPanel extends JPanel {
    private double protein, fat, carbs;

    public void updateData(double p, double f, double c) {
        this.protein = p;
        this.fat = f;
        this.carbs = c;
        repaint(); // Tells Java to redraw the component
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double total = protein + fat + carbs;
        if (total <= 0) {
            g2.drawString("No macronutrient data available for this day.", 50, 100);
            return;
        }

        double pAngle = (protein / total) * 360;
        double fAngle = (fat / total) * 360;
        double cAngle = (carbs / total) * 360;

        int diameter = Math.min(getWidth(), getHeight()) - 60;
        int x = (getWidth() - diameter) / 2;
        int y = (getHeight() - diameter) / 2;

        g2.setColor(new Color(100, 200, 100));
        g2.fill(new Arc2D.Double(x, y, diameter, diameter, 0, pAngle, Arc2D.PIE));

        g2.setColor(new Color(200, 100, 100));
        g2.fill(new Arc2D.Double(x, y, diameter, diameter, pAngle, fAngle, Arc2D.PIE));

        g2.setColor(new Color(100, 100, 200));
        g2.fill(new Arc2D.Double(x, y, diameter, diameter, pAngle + fAngle, cAngle, Arc2D.PIE));

        drawLegend(g2, "Protein (" + String.format("%.1f", protein) + "g)", new Color(100, 200, 100), 20);
        drawLegend(g2, "Fat (" + String.format("%.1f", fat) + "g)", new Color(200, 100, 100), 40);
        drawLegend(g2, "Carbs (" + String.format("%.1f", carbs) + "g)", new Color(100, 100, 200), 60);
    }

    private void drawLegend(Graphics2D g2, String text, Color color, int y) {
        g2.setColor(color);
        g2.fillRect(10, y, 10, 10);
        g2.setColor(Color.BLACK);
        g2.drawString(text, 25, y + 10);
    }
}