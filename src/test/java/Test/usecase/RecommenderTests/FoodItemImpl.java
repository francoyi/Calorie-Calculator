package Test.usecase.RecommenderTests;

import com.caloriecalc.entity.FoodItem;

public class FoodItemImpl implements FoodItem {
    private final String name;
    private final double kcalPerServing;

    public FoodItemImpl(String name, double kcalPerServing) {
        this.name = name;
        this.kcalPerServing = kcalPerServing;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public double kcalPerServing() {
        return kcalPerServing;
    }

    @Override
    public String getSource() {
        return "";
    }
}
