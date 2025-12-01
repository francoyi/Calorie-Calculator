package com.caloriecalc.interfaceadapters.AddMyFoodToMeal;

import com.caloriecalc.usecase.myfoods.addmyfoodtomeal.AddMyFoodToMealOutputBoundary;
import com.caloriecalc.usecase.myfoods.addmyfoodtomeal.AddMyFoodToMealOutputData;

public class AddMyFoodToMealPresenter implements AddMyFoodToMealOutputBoundary {

    private final MyFoodsSelectionViewModel vm;

    public AddMyFoodToMealPresenter(MyFoodsSelectionViewModel vm) {
        this.vm = vm;
    }

    @Override
    public void present(AddMyFoodToMealOutputData data) {
        vm.setSelectedFood(data);
    }
}