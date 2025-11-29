package com.caloriecalc.port.savetomyfood;

import com.caloriecalc.port.savetomyfood.SaveToMyFoodOutputData;

/**
 * Output Boundary for the SaveToMyFood use case.
 *
 * The Interactor calls this interface.
 * The Presenter implements this interface.
 */
public interface SaveToMyFoodOutputBoundary {
    void present(SaveToMyFoodOutputData data);
}