package com.caloriecalc.service;

import com.caloriecalc.model.*;
import com.caloriecalc.port.FoodLogRepository;
import com.caloriecalc.port.NutritionDataProvider;
import com.caloriecalc.util.UnitParser;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.UUID;

import com.caloriecalc.port.UserSettingsRepository;

public class FoodLogService {
    private final FoodLogRepository repo;
    private final NutritionDataProvider provider;
    private final UserSettingsRepository settingsRepo;
    private final ZoneId zone = ZoneId.of("America/Toronto");

    public FoodLogService(FoodLogRepository repo, NutritionDataProvider provider, UserSettingsRepository settingsRepo) {
        this.repo = repo;
        this.provider = provider;
        this.settingsRepo = settingsRepo;
    }

    public DailyLog getDay(LocalDate date) {
        DailyLog d = repo.getDay(date);
        if (d == null) {
            d = new DailyLog(date);
            d.setMeals(new ArrayList<>());
            d.setTotalKcal(0.0);
        }
        return d;
    }

    public Meal newEmptyMeal(LocalDate date, String label) {
        return new Meal(UUID.randomUUID().toString(), date, label);
    }

    /**
     * Build a MealEntry using FoodItem polymorphism; if API missing, kcal remains null for manual input later.
     */
    public MealEntry resolveEntryFromInput(String input) {
        UnitParser.Parsed parsed = UnitParser.parse(input);
        String name = parsed.name().trim();
        double amt = Double.isNaN(parsed.amount()) ? 100.0 : parsed.amount();
        String unit = parsed.unit() == null ? "g" : parsed.unit();
        NutritionValues per100 = provider.fetchNutritionPer100(name);
        FoodItem item;
        Double kcalServing;
        String source;
        if (per100 != null && per100.energyKcal() != null) {
            item = new APIFoodItem(name, per100, amt);
            kcalServing = item.kcalPerServing();
            source = item.getSource();
        } else {
            item = new LocalFoodItem(name, Double.NaN);
            kcalServing = null;
            source = item.getSource();
        }
        return new MealEntry(name, input, new Serving(amt, unit), kcalServing, source, ZonedDateTime.now(zone), per100);
    }

    public void saveMeal(LocalDate date, Meal meal) {
        double total = 0.0;
        for (MealEntry e : meal.getEntries()) if (e.kcalForServing() != null) total += e.kcalForServing();
        meal.setTotalKcal(total);
        DailyLog day = repo.getDay(date);
        if (day == null) {
            day = new DailyLog(date);
            day.setMeals(new ArrayList<>());
        }
        boolean replaced = false;
        for (int i = 0; i < day.getMeals().size(); i++) {
            if (day.getMeals().get(i).getId().equals(meal.getId())) {
                day.getMeals().set(i, meal);
                replaced = true;
                break;
            }
        }
        if (!replaced) day.getMeals().add(meal);
        double dayTotal = 0.0;
        for (Meal m : day.getMeals()) dayTotal += m.getTotalKcal();
        day.setTotalKcal(dayTotal);
        repo.saveDay(day);
    }

    public void deleteMeal(LocalDate date, String mealId) {
        DailyLog day = repo.getDay(date);
        if (day == null) return;
        day.getMeals().removeIf(m -> m.getId().equals(mealId));
        double dayTotal = 0.0;
        for (Meal m : day.getMeals()) dayTotal += m.getTotalKcal();
        day.setTotalKcal(dayTotal);
        repo.saveDay(day);
    }

    public UserSettings getSettings() {
        return settingsRepo.getSettings();
    }

    public void setDailyGoal(double kcal) {
        if (kcal <= 0) {
            throw new IllegalArgumentException("Goal must be a positive number.");
        }
        UserSettings settings = settingsRepo.getSettings();
        settings.setDailyKcalGoal(kcal);
        settingsRepo.saveSettings(settings);
    }

    public double getDailyGoal() {
        return settingsRepo.getSettings().getDailyKcalGoal();
    }

    public com.caloriecalc.port.FoodLogRepository getRepository() {
        return repo;
    }
}
