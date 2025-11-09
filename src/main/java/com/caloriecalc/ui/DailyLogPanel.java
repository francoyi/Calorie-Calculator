package com.caloriecalc.ui;

import com.caloriecalc.model.DailyLog;
import com.caloriecalc.model.Meal;
import com.caloriecalc.model.UserSettings;
import com.caloriecalc.service.FoodLogService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class DailyLogPanel extends JPanel {
    private final FoodLogService service;
    private final MainPanel mainPanel;

    public DailyLogPanel(FoodLogService service, MainPanel mainPanel){
        this.service = service;
        this.mainPanel = mainPanel;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10,10,10,10));
    }

    public void renderDay(DailyLog day, UserSettings settings){
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
        if (meals.isEmpty()){
            add(new JLabel("No meals recorded for this day."));
        } else {
            for (Meal m : meals){
                MealPanel mp = new MealPanel(service, m, mainPanel);
                mp.setOnChanged(() -> mainPanel.refresh());
                add(mp);
                add(Box.createVerticalStrut(10));
            }
        }
        revalidate();
        repaint();
    }
}