package com.caloriecalc.repo;

import com.caloriecalc.model.UserMetrics;
import com.caloriecalc.port.UserMetricsRepository;


import java.util.Optional;


public class JsonUserMetricsRepository implements UserMetricsRepository {

    @Override
    public Optional<UserMetrics> load() {
        return Optional.empty();
    }

    @Override
    public void save(UserMetrics metrics) {

    }
}