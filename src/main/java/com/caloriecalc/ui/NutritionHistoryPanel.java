package com.caloriecalc.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A Panel containing the History Graph and Navigation Controls.
 */
public class NutritionHistoryPanel extends JPanel {
    private Map<LocalDate, Double> data;
    private List<LocalDate> sortedDates;
    private double goal;

    private int scrollOffset = 0;
    private final int VISIBLE_DAYS = 7;

    private final GraphCanvas graphCanvas = new GraphCanvas();
    private final JButton prevBtn = new JButton("< Older");
    private final JButton nextBtn = new JButton("Newer >");
    private final JLabel rangeLabel = new JLabel("", SwingConstants.CENTER);

    public NutritionHistoryPanel() {
        setLayout(new BorderLayout());

        add(graphCanvas, BorderLayout.CENTER);

        JPanel controls = new JPanel(new BorderLayout());
        JPanel buttons = new JPanel(new FlowLayout());

        buttons.add(prevBtn);
        buttons.add(rangeLabel);
        buttons.add(nextBtn);

        controls.add(buttons, BorderLayout.CENTER);
        controls.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(controls, BorderLayout.SOUTH);

        prevBtn.addActionListener(e -> {
            scrollOffset++;
            refreshView();
        });

        nextBtn.addActionListener(e -> {
            if (scrollOffset > 0) scrollOffset--;
            refreshView();
        });
    }

    public void updateData(Map<LocalDate, Double> data, double goal) {
        this.data = data;
        this.goal = goal;
        this.sortedDates = new ArrayList<>(data.keySet());
        Collections.sort(sortedDates);

        this.scrollOffset = 0;
        refreshView();
    }

    private void refreshView() {
        if (sortedDates == null || sortedDates.isEmpty()) {
            rangeLabel.setText("No Data");
            prevBtn.setEnabled(false);
            nextBtn.setEnabled(false);
            graphCanvas.repaint();
            return;
        }

        int totalDays = sortedDates.size();
        int endIndex = totalDays - scrollOffset;
        int startIndex = Math.max(0, endIndex - VISIBLE_DAYS);

        endIndex = Math.max(startIndex, Math.min(endIndex, totalDays));

        prevBtn.setEnabled(startIndex > 0);
        nextBtn.setEnabled(endIndex < totalDays);

        if (startIndex < endIndex) {
            LocalDate start = sortedDates.get(startIndex);
            LocalDate end = sortedDates.get(endIndex - 1);
            rangeLabel.setText(String.format("%d/%d - %d/%d",
                    start.getMonthValue(), start.getDayOfMonth(),
                    end.getMonthValue(), end.getDayOfMonth()));
        } else {
            rangeLabel.setText("No Data in Range");
        }

        graphCanvas.repaint();
    }

    /**
     * Inner class that handles the actual drawing logic.
     */
    private class GraphCanvas extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (sortedDates == null || sortedDates.isEmpty()) {
                g.drawString("No history data available.", 50, 50);
                return;
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int padding = 60;
            int graphH = h - 2 * padding;
            int graphW = w - 2 * padding;

            int totalDays = sortedDates.size();
            int endIndex = totalDays - scrollOffset;
            int startIndex = Math.max(0, endIndex - VISIBLE_DAYS);

            if (startIndex >= endIndex) return;

            List<LocalDate> viewDates = sortedDates.subList(startIndex, endIndex);

            double maxVal = goal;
            for (double val : data.values()) maxVal = Math.max(maxVal, val);
            maxVal = Math.max(maxVal, 2500);

            g2.setColor(Color.GRAY);
            g2.drawLine(padding, h - padding, w - padding, h - padding);
            g2.drawLine(padding, padding, padding, h - padding);

            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2.drawString(String.format("%.0f", maxVal), 5, padding + 5);
            g2.drawString("0", 35, h - padding);

            int goalY = padding + (int) ((1 - (goal / maxVal)) * graphH);
            g2.setColor(new Color(220, 50, 50));
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(padding, goalY, w - padding, goalY);
            g2.drawString("Goal: " + (int)goal, w - padding - 80, goalY - 5);

            int numBars = viewDates.size();
            int slotWidth = graphW / VISIBLE_DAYS;
            int barWidth = (int)(slotWidth * 0.6);
            int barGap = (slotWidth - barWidth) / 2;

            for (int i = 0; i < numBars; i++) {
                LocalDate date = viewDates.get(i);
                double val = data.get(date);

                int barHeight = (int) ((val / maxVal) * graphH);
                int x = padding + (i * slotWidth) + barGap;
                int y = padding + graphH - barHeight;

                if (val > goal) g2.setColor(new Color(255, 165, 0));
                else g2.setColor(new Color(100, 150, 250));
                g2.fillRect(x, y, barWidth, barHeight);

                g2.setColor(Color.BLACK);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                FontMetrics fmNum = g2.getFontMetrics();
                String calStr = String.valueOf((int)val);
                int calTextX = x + (barWidth - fmNum.stringWidth(calStr)) / 2;
                g2.drawString(calStr, calTextX, y - 5);

                g2.setColor(Color.BLACK);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                FontMetrics fmDate = g2.getFontMetrics();
                String dateStr = date.getMonthValue() + "/" + date.getDayOfMonth();
                int dateTextX = x + (barWidth - fmDate.stringWidth(dateStr)) / 2;
                g2.drawString(dateStr, dateTextX, h - padding + 15);
            }
        }
    }
}