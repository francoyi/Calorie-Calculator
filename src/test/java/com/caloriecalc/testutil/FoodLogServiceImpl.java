package com.caloriecalc.testutil;

import com.caloriecalc.port.FoodLogRepository;
import com.caloriecalc.port.NutritionDataProvider;
import com.caloriecalc.port.UserSettingsRepository;
import com.caloriecalc.service.FoodLogService;
public class FoodLogServiceImpl extends FoodLogService {
    public FoodLogServiceImpl(FoodLogRepository repo, NutritionDataProvider provider, UserSettingsRepository settingsRepo) {
        super(repo, provider, settingsRepo);
    }

    @Override
    public double getDailyGoal() {
        return 2000.0; // Target 2000 kcal
    }
}
