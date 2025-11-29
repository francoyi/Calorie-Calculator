package com.caloriecalc.ui.myfoods;

import com.caloriecalc.port.listmyfoods.ListMyFoodsOutputBoundary;
import com.caloriecalc.port.listmyfoods.ListMyFoodsOutputData;

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