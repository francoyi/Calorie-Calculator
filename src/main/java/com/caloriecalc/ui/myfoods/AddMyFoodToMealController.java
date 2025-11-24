package com.caloriecalc.ui.myfoods;

import com.caloriecalc.model.MyFood;
import com.caloriecalc.port.addmyfoodtomeal.*;

public class AddMyFoodToMealController {

    private final AddMyFoodToMealInputBoundary interactor;

    public AddMyFoodToMealController(AddMyFoodToMealInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void addToMeal(MyFood food) {
        AddMyFoodToMealInputData data =
                new AddMyFoodToMealInputData(food.getName(), food.getTotalKcal());
        interactor.execute(data);
    }
}
