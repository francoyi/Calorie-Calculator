package com.caloriecalc.ui;

import com.caloriecalc.port.visualize.VisualizeNutritionInputBoundary;
import com.caloriecalc.port.visualize.VisualizeNutritionInputData;
import java.time.LocalDate;

public class VisualizeNutritionController {
    private final VisualizeNutritionInputBoundary interactor;

    public VisualizeNutritionController(VisualizeNutritionInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(LocalDate date) {
        VisualizeNutritionInputData inputData = new VisualizeNutritionInputData(date);
        interactor.execute(inputData);
    }
}