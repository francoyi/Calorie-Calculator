package com.caloriecalc.usecase.myfoods.savetomyfood;

/**
 * Output Boundary for the SaveToMyFood use case.
 *
 * The Interactor calls this interface.
 * The Presenter implements this interface.
 */
public interface SaveToMyFoodOutputBoundary {
    void present(SaveToMyFoodOutputData data);
}