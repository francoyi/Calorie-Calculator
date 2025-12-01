package com.caloriecalc.interfaceadapters.AddMyFoodToMeal;

import com.caloriecalc.entity.MyFood;
import com.caloriecalc.usecase.myfoods.addmyfoodtomeal.AddMyFoodToMealInputBoundary;
import com.caloriecalc.usecase.myfoods.addmyfoodtomeal.AddMyFoodToMealInputData;

public class AddMyFoodToMealController {

    private final AddMyFoodToMealInputBoundary interactor;

    public AddMyFoodToMealController(AddMyFoodToMealInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void addToMeal(MyFood food) {
        AddMyFoodToMealInputData data =
                new AddMyFoodToMealInputData(food.getName(), food.getTotalKcal(), food.getAmount(), food.getUnit());
        interactor.execute(data);
    }
}