
package com.caloriecalc.ui;
import com.caloriecalc.model.DailyLog;
import com.caloriecalc.model.Meal;
import com.caloriecalc.service.FoodLogService;
import javax.swing.*; import javax.swing.border.EmptyBorder;
import java.awt.*; import java.util.List;
public class DailyLogPanel extends JPanel {
  private final FoodLogService service;
  public DailyLogPanel(FoodLogService service){
    this.service = service; setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(new EmptyBorder(10,10,10,10));
  }
  public void renderDay(DailyLog day){
    removeAll();
    JLabel total = new JLabel(String.format("Total kcal: %.2f", day.getTotalKcal()));
    total.setFont(total.getFont().deriveFont(Font.BOLD, 14f)); add(total);
    add(Box.createVerticalStrut(10));
    List<Meal> meals = day.getMeals();
    if (meals.isEmpty()){ add(new JLabel("No meals recorded for this day.")); }
    else { for (Meal m : meals){ add(new MealPanel(service, m)); add(Box.createVerticalStrut(10)); } }
    revalidate(); repaint();
  }
}
