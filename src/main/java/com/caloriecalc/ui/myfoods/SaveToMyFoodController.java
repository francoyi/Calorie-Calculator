package com.caloriecalc.ui.myfoods;

import com.caloriecalc.model.Ingredient;
import com.caloriecalc.port.savetomyfood.SaveToMyFoodInputBoundary;
import com.caloriecalc.port.savetomyfood.SaveToMyFoodInputData;

import java.util.List;

/**
 * Controller for the SaveToMyFood use case.
 *
 * Receives user input from the UI (CreateFoodDialog),
 * translates it into SaveToMyFoodInputData,
 * and invokes the SaveToMyFoodInteractor via the Input Boundary.
 */
public class SaveToMyFoodController {

    private final SaveToMyFoodInputBoundary interactor;

    public SaveToMyFoodController(SaveToMyFoodInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Called by the UI when a user chooses "OK and save to My Foods".
     */
    public void save(String name, List<Ingredient> ingredients) {
        SaveToMyFoodInputData data = new SaveToMyFoodInputData(name, ingredients);
        interactor.execute(data);
    }

    public void execute(SaveToMyFoodInputData data) {
        interactor.execute(data);
    }
}