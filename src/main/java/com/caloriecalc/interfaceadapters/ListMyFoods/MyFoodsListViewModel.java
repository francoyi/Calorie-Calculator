package com.caloriecalc.interfaceadapters.ListMyFoods;

import com.caloriecalc.entity.MyFood;

import java.util.ArrayList;
import java.util.List;

public class MyFoodsListViewModel {

    private final List<MyFood> foods = new ArrayList<>();

    public void setFoods(List<MyFood> list) {
        foods.clear();
        foods.addAll(list);
    }

    public List<MyFood> getFoods() {
        return foods;
    }
}