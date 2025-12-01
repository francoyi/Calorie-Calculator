package com.caloriecalc.port.tdee;

import com.caloriecalc.model.UserMetrics;

// Raw Output - BMR formula is ALLOWED to output negative numbers!!! Anything using this must handle it as such.

/**
 * Represents a template to compute a user's Basal Metabolic Rate (BMR).
 * The formula's name is exposed as {@link #name()} for displaying or logging
 * A BMR formula implementation is permitted to return <strong>negative</strong> values.
 *  * This interface makes no guarantees about output validity. Any component
 *  * consuming {@code computeBmr(UserMetrics)} must handle negative values
 *  * appropriately
 */

public interface BmrFormula {
    /**
     * Returns the human-readable name of this BMR formula.
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
     *
     * @return the computed BMR (may be negative)
     */

    double computeBmr(UserMetrics userMetrics);
}
