package com.caloriecalc.ui;

import com.caloriecalc.entity.DailyLog;
import com.caloriecalc.entity.Meal;
import com.caloriecalc.entity.UserSettings;
import com.caloriecalc.usecase.foodcalorielookup.FoodLogService;
import com.caloriecalc.entity.MealRecommendationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class DailyLogPanel extends JPanel {
    private final FoodLogService service;
    private final MainPanel mainPanel;
    private final MealRecommendationService mealRecommendationService;

    public DailyLogPanel(FoodLogService service, MealRecommendationService mealRecommendationService, MainPanel mainPanel) {
        this.service = service;
        this.mainPanel = mainPanel;
        this.mealRecommendationService = mealRecommendationService;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    public void renderDay(DailyLog day, UserSettings settings) {
        removeAll();

        double totalKcal = day.getTotalKcal();
        double goalKcal = settings.getDailyKcalGoal();
        String progress = String.format("Total kcal: %.2f / %.2f", totalKcal, goalKcal);

        JLabel total = new JLabel(progress);
        total.setFont(total.getFont().deriveFont(Font.BOLD, 14f));
        if (totalKcal > goalKcal) {
            total.setForeground(Color.RED);
        }
        add(total);
        add(Box.createVerticalStrut(10));

        List<Meal> meals = day.getMeals();
        if (meals.isEmpty()) {
            add(new JLabel("No meals recorded for this day."));
        } else {
            for (Meal m : meals) {
                MealPanel mp = new MealPanel(service, mealRecommendationService, m, mainPanel);
                mp.setOnChanged(() -> mainPanel.refresh());
                add(mp);
                add(Box.createVerticalStrut(10));
            }
        }
        revalidate();
        repaint();
    }
}