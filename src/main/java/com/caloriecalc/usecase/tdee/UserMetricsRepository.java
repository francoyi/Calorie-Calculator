package com.caloriecalc.usecase.tdee;

import java.util.Optional;

import com.caloriecalc.entity.UserMetrics;

public interface UserMetricsRepository {

    /**
     * Loads the most recently saved {@link UserMetrics}, if present.
     *
     * @return an {@link Optional} containing the stored user metrics,
     *         or {@code Optional.empty()} if none have been saved yet
     */
    Optional<UserMetrics> load();

    /**
     * Persists the given {@link UserMetrics}, overwriting any previously stored value.
     *
     * @param metrics the metrics object to save; must not be {@code null}
     */
    void save(UserMetrics metrics);
}
