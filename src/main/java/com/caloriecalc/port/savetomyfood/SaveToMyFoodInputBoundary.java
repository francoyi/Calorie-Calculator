package com.caloriecalc.port.savetomyfood;

/**
 * Input Boundary for the SaveToMyFood use case.
 *
 * The Controller calls this interface.
 * The Interactor implements this interface.
 */
public interface SaveToMyFoodInputBoundary {
    void execute(SaveToMyFoodInputData data);
}