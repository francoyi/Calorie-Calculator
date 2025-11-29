package com.caloriecalc.ui.myfoods;

import com.caloriecalc.port.listmyfoods.ListMyFoodsInputBoundary;
import com.caloriecalc.port.listmyfoods.ListMyFoodsInputData;

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