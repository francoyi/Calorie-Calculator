package com.caloriecalc.port;

import com.caloriecalc.model.UserMetrics;

import java.util.Optional;

public interface UserMetricsRepository {

    /**
     * Load the last saved metrics, if there are any.
     */
    Optional<UserMetrics> load();

    /**
     * Save (or overwrite) the current metrics.
     */
    void save(UserMetrics metrics);
}