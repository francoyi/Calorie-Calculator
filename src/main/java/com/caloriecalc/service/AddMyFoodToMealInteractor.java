package com.caloriecalc.service;

import com.caloriecalc.port.addmyfoodtomeal.*;

public class AddMyFoodToMealInteractor implements AddMyFoodToMealInputBoundary {

    private final AddMyFoodToMealOutputBoundary presenter;

    public AddMyFoodToMealInteractor(AddMyFoodToMealOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(AddMyFoodToMealInputData data) {
        AddMyFoodToMealOutputData out =
                new AddMyFoodToMealOutputData(
                        data.getFoodName(),
                        100.0,
                        "g",
                        data.getTotalKcal()
                );

        presenter.present(out);
    }
}