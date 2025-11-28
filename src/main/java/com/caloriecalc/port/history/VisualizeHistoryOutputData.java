package com.caloriecalc.port.history;
import java.time.LocalDate;
import java.util.Map;

public record VisualizeHistoryOutputData(
        Map<LocalDate, Double> caloriesPerDay,
        double calorieGoal
) {}