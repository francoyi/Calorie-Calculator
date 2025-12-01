package com.caloriecalc.entity.util;

public final class EnergyConverter {
    private EnergyConverter() {
    }

    public static double kjToKcal(double kj) {
        return kj * 0.239005736;
    }
}
