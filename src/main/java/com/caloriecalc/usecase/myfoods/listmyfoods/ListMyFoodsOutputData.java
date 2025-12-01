package com.caloriecalc.usecase.myfoods.listmyfoods;

import com.caloriecalc.entity.MyFood;
import java.util.List;

public class ListMyFoodsOutputData {

    private final List<MyFood> foods;

    public ListMyFoodsOutputData(List<MyFood> foods) {
        this.foods = foods;
    }

    public List<MyFood> getFoods() {
        return foods;
    }
}