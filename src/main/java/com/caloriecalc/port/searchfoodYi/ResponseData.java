package com.caloriecalc.port.searchfoodYi;

public record ResponseData(
        String name,
        double amount,
        String unit,
        Double kcalPerServing

) {}
