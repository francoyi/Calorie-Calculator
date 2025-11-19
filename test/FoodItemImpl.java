import com.caloriecalc.model.FoodItem;

class FoodItemImpl implements FoodItem {
    private final String name;
    private final double kcalPerServing;

    public FoodItemImpl(String name, double kcalPerServing) {
        this.name = name;
        this.kcalPerServing = kcalPerServing;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getKcalPerServing() {
        return kcalPerServing;
    }

    @Override
    public String getSource() {
        return "";
    }
}
