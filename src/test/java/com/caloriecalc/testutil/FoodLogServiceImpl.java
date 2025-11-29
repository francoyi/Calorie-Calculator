package com.caloriecalc.testutil;

import com.caloriecalc.port.FoodLogRepository;
import com.caloriecalc.port.NutritionDataProvider;
import com.caloriecalc.port.UserSettingsRepository;
import com.caloriecalc.service.FoodLogService;
import com.caloriecalc.repo.InMemoryMyFoodRepository;
public class FoodLogServiceImpl extends FoodLogService {
    public FoodLogServiceImpl(FoodLogRepository repo, NutritionDataProvider provider, UserSettingsRepository settingsRepo) {
        super(repo, provider, settingsRepo, new InMemoryMyFoodRepository());
    }

    @Override
    public double getDailyGoal() {
        return 2000.0; // Target 2000 kcal
    }
}
