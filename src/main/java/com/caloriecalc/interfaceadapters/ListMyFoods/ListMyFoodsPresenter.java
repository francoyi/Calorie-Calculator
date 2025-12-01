package com.caloriecalc.interfaceadapters.ListMyFoods;

import com.caloriecalc.usecase.myfoods.listmyfoods.ListMyFoodsOutputBoundary;
import com.caloriecalc.usecase.myfoods.listmyfoods.ListMyFoodsOutputData;

public class ListMyFoodsPresenter implements ListMyFoodsOutputBoundary {

    private final MyFoodsListViewModel viewModel;

    public ListMyFoodsPresenter(MyFoodsListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(ListMyFoodsOutputData outputData) {
        viewModel.setFoods(outputData.getFoods());
    }
}