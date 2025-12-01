package com.caloriecalc.usecase.searchfood;

public record RequestData(
        String name,
        double amount,
        String unit
) {}

