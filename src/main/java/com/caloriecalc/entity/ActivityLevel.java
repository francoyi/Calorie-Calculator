package com.caloriecalc.entity;

//ballpark vals used here
public enum ActivityLevel {
    VERY_LIGHT(1.2),
    LIGHT(1.39),
    MEDIUM(1.57),
    HIGH(1.75),
    EXTREME(1.9);

    public final double multiplier;

    ActivityLevel(double multiplier) {

        this.multiplier = multiplier;
    }
}
