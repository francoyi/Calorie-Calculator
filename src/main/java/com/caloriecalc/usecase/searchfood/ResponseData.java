package com.caloriecalc.usecase.searchfood;

public record ResponseData(
        String name,
        double amount,
        String unit,
        Double kcalPerServing

) {}
