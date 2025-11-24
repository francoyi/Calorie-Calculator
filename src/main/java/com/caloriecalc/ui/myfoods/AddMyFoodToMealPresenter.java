package com.caloriecalc.ui.myfoods;

import com.caloriecalc.port.addmyfoodtomeal.*;

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
