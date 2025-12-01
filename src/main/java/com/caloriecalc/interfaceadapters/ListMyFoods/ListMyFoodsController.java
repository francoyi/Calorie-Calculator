package com.caloriecalc.interfaceadapters.ListMyFoods;

import com.caloriecalc.usecase.myfoods.listmyfoods.ListMyFoodsInputBoundary;
import com.caloriecalc.usecase.myfoods.listmyfoods.ListMyFoodsInputData;

public class ListMyFoodsController {

    private final ListMyFoodsInputBoundary interactor;

    public ListMyFoodsController(ListMyFoodsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void listFoods() {
        ListMyFoodsInputData inputData = new ListMyFoodsInputData();
        interactor.execute(inputData);
    }
}