package com.caloriecalc.usecase.tdee;

public interface CalculateTdeeOutputBoundary {
    /**
     * Presents a successful TDEE calculation result.
     *
     * <p>
     * Implementations should translate the raw output data into
     * a form suitable for the user interface (view models, strings,
     * formatting, etc.).
     *
     * @param output the response model produced by the interactor;
     *               must not be {@code null}
     */
    void present(CalculateTdeeOutputData output);

    /**
     * Presents an error resulting from invalid input or an inability
     * to execute the use case.
     *
     * <p>
     * Typical implementations may display an error message to the user,
     * log the issue, or route the message through a presenter-specific
     * formatting pipeline.
     *
     * @param message a human-readable validation error message;
     *                must not be {@code null}
     */
    void presentValidationError(String message);
}
