package com.caloriecalc.usecase.myfoods.listmyfoods;

import com.caloriecalc.entity.MyFood;
import com.caloriecalc.usecase.myfoods.MyFoodRepository;

import java.util.List;

public class ListMyFoodsInteractor implements ListMyFoodsInputBoundary {

    private final MyFoodRepository repository;
    private final ListMyFoodsOutputBoundary presenter;

    public ListMyFoodsInteractor(MyFoodRepository repository,
                                 ListMyFoodsOutputBoundary presenter) {
        this.repository = repository;
        this.presenter = presenter;
    }

    @Override
    public void execute(ListMyFoodsInputData inputData) {
        // Retrieve all saved foods from repo
        List<MyFood> foods = repository.getAllFoods();

        // Wrap data in OutputData
        ListMyFoodsOutputData outputData = new ListMyFoodsOutputData(foods);

        // Send to presenter
        presenter.present(outputData);
    }
}