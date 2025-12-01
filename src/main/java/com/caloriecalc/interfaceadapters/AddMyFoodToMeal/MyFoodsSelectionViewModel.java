package com.caloriecalc.interfaceadapters.AddMyFoodToMeal;

import com.caloriecalc.usecase.myfoods.addmyfoodtomeal.AddMyFoodToMealOutputData;

public class MyFoodsSelectionViewModel {

    private AddMyFoodToMealOutputData selectedFood;

    public void setSelectedFood(AddMyFoodToMealOutputData data) {
        this.selectedFood = data;
    }

    public AddMyFoodToMealOutputData getSelectedFood() {
        return selectedFood;
    }

    public boolean hasSelection() {
        return selectedFood != null;
    }
}