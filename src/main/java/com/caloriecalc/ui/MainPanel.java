package com.caloriecalc.ui;

import com.caloriecalc.Main;
import com.caloriecalc.entity.DailyLog;
import com.caloriecalc.entity.UserSettings;
import com.caloriecalc.interfaceadapters.TDEE.VisualizeHistoryController;
import com.caloriecalc.interfaceadapters.VisualizeHistory.VisualizeHistoryPresenter;
import com.caloriecalc.usecase.myfoods.MyFoodRepository;
import com.caloriecalc.usecase.tdee.UserMetricsRepository;
import com.caloriecalc.usecase.foodcalorielookup.FoodLogService;
import com.caloriecalc.entity.MealRecommendationService;
import com.caloriecalc.usecase.history.VisualizeHistoryInteractor;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;

public class MainPanel extends JPanel {
    private final ZoneId ZONE = ZoneId.of("America/Toronto");
    private final JButton prevBtn = new JButton("<");
    private final JButton todayBtn = new JButton("Today");
    private final JButton nextBtn = new JButton(">");
    private final JButton addMealBtn = new JButton("Add Meal");
    private final JButton setGoalBtn = new JButton("Set Goal");
    private final JButton themeSwitchBtn = new JButton("Toggle Theme");
    private final JButton calExpecBtn = new JButton("Daily Calorie Burn Helper");
    private final JButton historyBtn = new JButton("History Graph");
    private final JLabel dateLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel goalLabel = new JLabel("", SwingConstants.CENTER);
    private final UserMetricsRepository metricsRepo;
    private LocalDate current = LocalDate.now(ZONE);
    private final FoodLogService service;
    private final MealRecommendationService mealRecommendationService;
    private final DailyLogPanel dailyPanel;
    private final MyFoodRepository myFoodRepo;
    private DailyLog lastRendered;
    // UserMetricsRepository metricsRepo
    public MainPanel(FoodLogService service, MealRecommendationService mealRecommendationService, UserMetricsRepository metricsRepo, MyFoodRepository myFoodRepo) {
        this.service = service;
        this.mealRecommendationService = mealRecommendationService;
        this.dailyPanel = new DailyLogPanel(service, mealRecommendationService, this);
        this.metricsRepo = metricsRepo;
        this.myFoodRepo = myFoodRepo;


        setLayout(new BorderLayout(8, 8));

        JPanel top = new JPanel(new BorderLayout());
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nav.add(prevBtn);
        nav.add(todayBtn);
        nav.add(nextBtn);
        top.add(nav, BorderLayout.WEST);

        JPanel centerTop = new JPanel();
        centerTop.setLayout(new BoxLayout(centerTop, BoxLayout.Y_AXIS));
        dateLabel.setFont(dateLabel.getFont().deriveFont(Font.BOLD, 16f));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        goalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerTop.add(dateLabel);
        centerTop.add(goalLabel);
        top.add(centerTop, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(addMealBtn);
        actions.add(setGoalBtn);
        actions.add(historyBtn);
        actions.add(themeSwitchBtn);
        actions.add(calExpecBtn);
        top.add(actions, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(dailyPanel);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        add(sp, BorderLayout.CENTER);

        prevBtn.addActionListener(e -> {
            current = current.minusDays(1);
            refresh();
        });
        nextBtn.addActionListener(e -> {
            current = current.plusDays(1);
            refresh();
        });
        todayBtn.addActionListener(e -> {
            current = LocalDate.now(ZONE);
            refresh();
        });
        addMealBtn.addActionListener(e -> onAddMeal());
        setGoalBtn.addActionListener(e -> onSetGoal());
        calExpecBtn.addActionListener(e -> openCalcTDEE());
        themeSwitchBtn.addActionListener(e -> onSwitchTheme());
        historyBtn.addActionListener(e -> onHistory());
        refresh();
    }

    private void openCalcTDEE() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        new TDEEView(owner, service, metricsRepo, this::refresh).setVisible(true);
    }



    private void onSwitchTheme() {
        Main.toggleTheme();
    }

    private void onSetGoal() {
        UserSettings currentSettings = service.getSettings();
        String input = JOptionPane.showInputDialog(
                this,
                "Enter your daily calorie goal:",
                currentSettings.getDailyKcalGoal()
        );
        if (input != null && !input.trim().isEmpty()) {
            try {
                double goal = Double.parseDouble(input);
                service.setDailyGoal(goal);
                refresh();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onAddMeal() {
        MealDialog dlg = new MealDialog(SwingUtilities.getWindowAncestor(this), service, current, null, mealRecommendationService,myFoodRepo);
        dlg.setVisible(true);
        refresh();
    }

    public void refresh() {
        dateLabel.setText(current.toString());
        DailyLog d = service.getDay(current);
        UserSettings s = service.getSettings();
        goalLabel.setText(String.format("Goal: %.2f kcal", s.getDailyKcalGoal()));

        dailyPanel.renderDay(d, s);
        lastRendered = d;
    }

    private void onHistory() {
        VisualizeHistoryPresenter presenter = new VisualizeHistoryPresenter(this);

        VisualizeHistoryInteractor interactor = new VisualizeHistoryInteractor(
                service.getRepository(),
                service.getSettingsRepo(),
                presenter
        );

        VisualizeHistoryController controller = new VisualizeHistoryController(interactor);
        controller.execute();
    }


}