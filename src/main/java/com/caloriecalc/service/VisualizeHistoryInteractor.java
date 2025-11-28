package com.caloriecalc.service;

import com.caloriecalc.model.DailyLog;
import com.caloriecalc.port.FoodLogRepository;
import com.caloriecalc.port.UserSettingsRepository;
import com.caloriecalc.port.history.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Interactor for the History Graph Use Case.
 * <p>
 * Fetches all daily logs, extracts the total calories, and retrieves the
 * user's daily calorie goal to allow for comparison.
 * </p>
 */
public class VisualizeHistoryInteractor implements VisualizeHistoryInputBoundary {
    private final FoodLogRepository foodLogRepo;
    private final UserSettingsRepository settingsRepo;
    private final VisualizeHistoryOutputBoundary presenter;

    public VisualizeHistoryInteractor(FoodLogRepository foodLogRepo,
                                      UserSettingsRepository settingsRepo,
                                      VisualizeHistoryOutputBoundary presenter) {
        this.foodLogRepo = foodLogRepo;
        this.settingsRepo = settingsRepo;
        this.presenter = presenter;
    }

    @Override
    public void execute(VisualizeHistoryInputData input) {
        List<DailyLog> allDays = foodLogRepo.getAllDays();
        double currentGoal = settingsRepo.getSettings().getDailyKcalGoal();

        Map<LocalDate, Double> historyMap = new TreeMap<>();

        for (DailyLog log : allDays) {
            if (log.getTotalKcal() > 0) {
                historyMap.put(log.getDate(), log.getTotalKcal());
            }
        }

        if (historyMap.isEmpty()) {
            presenter.prepareFailView("No history data found.");
            return;
        }

        presenter.present(new VisualizeHistoryOutputData(historyMap, currentGoal));
    }
}