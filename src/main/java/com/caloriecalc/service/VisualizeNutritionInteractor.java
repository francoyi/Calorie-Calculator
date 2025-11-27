package com.caloriecalc.service;

import com.caloriecalc.model.DailyLog;
import com.caloriecalc.model.Meal;
import com.caloriecalc.model.MealEntry;
import com.caloriecalc.port.FoodLogRepository;
import com.caloriecalc.port.visualize.VisualizeNutritionInputBoundary;
import com.caloriecalc.port.visualize.VisualizeNutritionInputData;
import com.caloriecalc.port.visualize.VisualizeNutritionOutputBoundary;
import com.caloriecalc.port.visualize.VisualizeNutritionOutputData;

/**
 * The Interactor for UC 12: Graphs showing user nutrition components.
 * <p>
 * This class implements the business logic for aggregating macronutrient data.
 * It retrieves the daily log, iterates through meals, sums up protein, fat, and carbs,
 * and passes the result to the Presenter.
 * </p>
 */
public class VisualizeNutritionInteractor implements VisualizeNutritionInputBoundary {
    private final FoodLogRepository foodLogRepository;
    private final VisualizeNutritionOutputBoundary presenter;

    public VisualizeNutritionInteractor(FoodLogRepository foodLogRepository,
                                        VisualizeNutritionOutputBoundary presenter) {
        this.foodLogRepository = foodLogRepository;
        this.presenter = presenter;
    }

    @Override
    public void execute(VisualizeNutritionInputData input) {
        DailyLog log = foodLogRepository.getDay(input.date());

        if (log == null || log.getMeals().isEmpty()) {
            presenter.prepareFailView("No meal data found for " + input.date());
            return;
        }

        double totalProtein = 0.0;
        double totalFat = 0.0;
        double totalCarbs = 0.0;
        double totalCalories = 0.0;

        for (Meal meal : log.getMeals()) {
            for (MealEntry entry : meal.getEntries()) {

                if (entry.kcalForServing() != null) {
                    totalCalories += entry.kcalForServing();
                }

                if (entry.nutritionPer100g() != null && entry.serving() != null) {
                    double ratio = entry.serving().amount() / 100.0;

                    if (entry.nutritionPer100g().proteinG() != null) {
                        totalProtein += entry.nutritionPer100g().proteinG() * ratio;
                    }
                    if (entry.nutritionPer100g().fatG() != null) {
                        totalFat += entry.nutritionPer100g().fatG() * ratio;
                    }
                    if (entry.nutritionPer100g().carbG() != null) {
                        totalCarbs += entry.nutritionPer100g().carbG() * ratio;
                    }
                }
            }
        }

        VisualizeNutritionOutputData output = new VisualizeNutritionOutputData(
                totalProtein, totalFat, totalCarbs, totalCalories
        );
        presenter.present(output);
    }
}