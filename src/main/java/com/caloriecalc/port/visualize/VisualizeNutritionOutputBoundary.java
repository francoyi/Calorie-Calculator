package com.caloriecalc.port.visualize;

/**
 * The Output Boundary for the Visualize Nutrition Use Case.
 * <p>
 * This interface defines how the Interactor sends results back to the UI.
 * Implementing this allows the Presenter to format the data for the View.
 * </p>
 */
public interface VisualizeNutritionOutputBoundary {
    /**
     * Presents the successful nutrition breakdown.
     *
     * @param output The calculated macronutrient totals.
     */
    void present(VisualizeNutritionOutputData output);

    /**
     * Prepares the failure view when data cannot be retrieved.
     *
     * @param error The error message to display.
     */
    void prepareFailView(String error);
}