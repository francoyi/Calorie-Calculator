package com.caloriecalc.port.listmyfoods;

import com.caloriecalc.model.MyFood;
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
