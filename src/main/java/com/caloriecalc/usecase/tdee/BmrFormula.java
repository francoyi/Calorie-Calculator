package com.caloriecalc.usecase.tdee;

import com.caloriecalc.entity.UserMetrics;

// Raw Output - BMR formula is ALLOWED to output negative numbers!!! Anything using this must handle it as such.
/**
 * Represents a Strategy for computing a user's Basal Metabolic Rate (BMR).
 *
 * <p>
 * Implementations of this interface provide different BMR formulas
 * (e.g., Mifflin–St Jeor, Harris–Benedict).
 */
public interface BmrFormula {
    /**
     * Returns the human-readable name of this BMR formula.
     *
     * <p>
     * Examples: {@code "Mifflin-St Jeor"}, {@code "Harris-Benedict"}.
     *
     * @return the formula name, never {@code null}
     */
    String name();

    /**
     * Computes the Basal Metabolic Rate (BMR) for the given user.
     *
     * <p>
     * Implementations may return negative values. Consumers must
     * explicitly validate or guard against such values if they are
     * not acceptable for downstream logic.
     *
     * @param userMetrics the user's metrics (age, sex, height, weight, etc.);
     *                    must not be {@code null}
     * @return the computed BMR (may be negative)
     */
    double computeBmr(UserMetrics userMetrics);
}
