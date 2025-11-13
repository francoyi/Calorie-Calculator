package com.caloriecalc.model;

//ballpark vals used here
public class ActivityLevel {
    double VERY_LIGHT = 1.2;
    double LIGHT = 1.39;
    double MEDIUM = 1.57;
    double HIGH = 1.75;
    double EXTREME = 1.9;

    private final double multiplier;
    ActivityLevel(double multiplier) {
        this.multiplier = multiplier;
    }
}
