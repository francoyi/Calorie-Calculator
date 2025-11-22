package com.caloriecalc.repo;

import com.caloriecalc.model.UserMetrics;
import com.caloriecalc.port.UserMetricsRepository;

import java.nio.file.Path;
import java.util.Optional;

public class JsonUserMetricsRepository
        extends AbstractJsonRepository<UserMetrics>
        implements UserMetricsRepository {

    public JsonUserMetricsRepository(Path file) {
        super(file);
    }

    @Override
    public Optional<UserMetrics> load() {
        UserMetrics metrics = loadOrDefault(null, UserMetrics.class);
        return Optional.ofNullable(metrics);
    }

    @Override
    public void save(UserMetrics metrics) {
        if (metrics == null) {
            throw new IllegalArgumentException("metrics must not be null");
        }
        atomicWrite(metrics);
    }
}
