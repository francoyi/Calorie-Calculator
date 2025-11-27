package com.caloriecalc.port.visualize;

/**
 * The Input Boundary for the Visualize Nutrition Use Case.
 * <p>
 * This interface defines the standard method for triggering the visualization logic.
 * It acts as a boundary between the Controller (UI layer) and the Interactor (Business Logic).
 * </p>
 */
public interface VisualizeNutritionInputBoundary {
    /**
     * Executes the visualization use case.
     *
     * @param input The input data containing the date to visualize.
     */
    void execute(VisualizeNutritionInputData input);
}