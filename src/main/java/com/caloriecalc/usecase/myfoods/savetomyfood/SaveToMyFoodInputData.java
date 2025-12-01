package com.caloriecalc.usecase.myfoods.savetomyfood;

import com.caloriecalc.entity.Ingredient;
import java.util.List;

/**
 * The interactor will compute total kcal internally.
 */
public class SaveToMyFoodInputData {

    private final String name;
    private final List<Ingredient> ingredients;

    public SaveToMyFoodInputData(String name, List<Ingredient> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}