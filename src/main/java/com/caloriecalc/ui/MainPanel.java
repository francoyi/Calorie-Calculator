
package com.caloriecalc.ui;
import com.caloriecalc.model.DailyLog;
import com.caloriecalc.port.FoodLogRepository;
import com.caloriecalc.port.NutritionDataProvider;
import com.caloriecalc.repo.JsonFoodLogRepository;
import com.caloriecalc.service.FoodLogService;
import com.caloriecalc.service.OpenFoodFactsClient;
import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
public class MainPanel extends JPanel {
  private final ZoneId ZONE = ZoneId.of("America/Toronto");
  private final JButton prevBtn = new JButton("<");
  private final JButton todayBtn = new JButton("Today");
  private final JButton nextBtn = new JButton(">");
  private final JButton addMealBtn = new JButton("Add Meal");
  private final JLabel dateLabel = new JLabel("", SwingConstants.CENTER);
  private LocalDate current = LocalDate.now(ZONE);
  private final FoodLogService service;
  private final DailyLogPanel dailyPanel;

  private DailyLog lastRendered;

  public MainPanel(){
    setLayout(new BorderLayout(8,8));
    FoodLogRepository repo = new JsonFoodLogRepository(Path.of("data","food_log.json"));
    NutritionDataProvider provider = new OpenFoodFactsClient();
    this.service = new FoodLogService(repo, provider);
    this.dailyPanel = new DailyLogPanel(service);
    JPanel top = new JPanel(new BorderLayout());
    JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT));
    nav.add(prevBtn);
    nav.add(todayBtn);
    nav.add(nextBtn);
    top.add(nav, BorderLayout.WEST);
    dateLabel.setFont(dateLabel.getFont().deriveFont(Font.BOLD, 16f));
    top.add(dateLabel, BorderLayout.CENTER);
    JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    actions.add(addMealBtn);
    top.add(actions, BorderLayout.EAST);
    add(top, BorderLayout.NORTH);
    add(new JScrollPane(dailyPanel), BorderLayout.CENTER);
    prevBtn.addActionListener(e -> { current = current.minusDays(1); refresh(); });
    nextBtn.addActionListener(e -> { current = current.plusDays(1); refresh(); });
    todayBtn.addActionListener(e -> { current = LocalDate.now(ZONE); refresh(); });
    addMealBtn.addActionListener(e -> onAddMeal());

    JScrollPane sp = new JScrollPane(dailyPanel);
    sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    sp.getVerticalScrollBar().setUnitIncrement(16);
    add(sp, BorderLayout.CENTER);
    refresh();
  }
  private void onAddMeal(){ MealDialog dlg = new MealDialog(SwingUtilities.getWindowAncestor(this), service, current, null);
    dlg.setVisible(true); refresh(); }

  private void refresh(){
    dateLabel.setText(current.toString());
    DailyLog d = service.getDay(current);
    if ((d == null || d.getMeals() == null || d.getMeals().isEmpty())
            && lastRendered != null
            && current.equals(lastRendered.getDate())) {
      dailyPanel.renderDay(lastRendered);
    } else {
      dailyPanel.renderDay(d);
      lastRendered = d;
    }
  }
}
