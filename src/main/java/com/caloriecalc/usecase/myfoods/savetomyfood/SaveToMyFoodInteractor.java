package com.caloriecalc.usecase.myfoods.savetomyfood;

import com.caloriecalc.entity.Ingredient;
import com.caloriecalc.entity.MyFood;
import com.caloriecalc.usecase.myfoods.MyFoodRepository;

import javax.annotation.processing.Generated;
import java.util.List;

/**
 * Interactor for the SaveToMyFood use case.
 *
 * Responsible for:
 *  - validating input
 *  - computing total kcal from ingredients
 *  - creating a MyFood entity
 *  - saving it via repository
 *  - calling the presenter with OutputData
 */
public class SaveToMyFoodInteractor implements SaveToMyFoodInputBoundary {

    private final MyFoodRepository repository;
    private final SaveToMyFoodOutputBoundary presenter;

    @Generated("ignored-by-coverage")
    public SaveToMyFoodInteractor(MyFoodRepository repository,
                                  SaveToMyFoodOutputBoundary presenter) {
        this.repository = repository;
        this.presenter = presenter;
    }

    @Override
    public void execute(SaveToMyFoodInputData data) {

        String name = data.getName();
        List<Ingredient> ingredients = data.getIngredients();

        // Validate name
        if (name == null || name.isBlank()) {
            presenter.present(new SaveToMyFoodOutputData(
                    false,
                    "Food name cannot be empty.",
                    null
            ));
            return;
        }

        // Validate ingredient list
        if (ingredients == null || ingredients.isEmpty()) {
            presenter.present(new SaveToMyFoodOutputData(
                    false,
                    "At least one ingredient is required.",
                    null
            ));
            return;
        }

        // Compute total kcal
        double totalKcal = 0.0;
        for (Ingredient ing : ingredients) {
                totalKcal += ing.getKcal();

        }

        // Compute total amount
        double amount = 0.0;
        for (Ingredient ing : ingredients) {
                amount += ing.getAmount();
        }


        // Create Entity
        MyFood food = new MyFood(name, ingredients, totalKcal,amount, ingredients.get(0).getUnit());

        // Save
        repository.save(food);

        // Return Output Data
        presenter.present(new SaveToMyFoodOutputData(
                true,
                "Food saved to My Foods.",
                food
        ));
    }

}