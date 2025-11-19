package com.caloriecalc.repo;

import com.caloriecalc.model.UserMetrics;
import com.caloriecalc.port.UserMetricsRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUserMetricsRepository implements UserMetricsRepository {

    @Override
    public Optional<UserMetrics> load() {
        return Optional.empty();
    }

    @Override
    public void save(UserMetrics metrics) {

    }
}