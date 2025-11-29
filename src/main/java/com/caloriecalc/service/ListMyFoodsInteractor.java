package com.caloriecalc.service;

import com.caloriecalc.model.MyFood;
import com.caloriecalc.port.MyFoodRepository;
import com.caloriecalc.port.listmyfoods.ListMyFoodsInputBoundary;
import com.caloriecalc.port.listmyfoods.ListMyFoodsInputData;
import com.caloriecalc.port.listmyfoods.ListMyFoodsOutputBoundary;
import com.caloriecalc.port.listmyfoods.ListMyFoodsOutputData;

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