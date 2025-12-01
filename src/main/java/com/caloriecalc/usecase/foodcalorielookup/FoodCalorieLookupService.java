package com.caloriecalc.usecase.foodcalorielookup;

import com.caloriecalc.entity.MealEntry;

public class FoodCalorieLookupService {

    private final FoodLogService service;

    public FoodCalorieLookupService(FoodLogService service) {
        this.service = service;
    }

    public Double lookupKcal(String name, double amount, String unit) {
        try {
            String input = name + " " + amount + (unit == null ? "" : unit);
            MealEntry entry = service.resolveEntryFromInput(input);
            return entry.kcalForServing();
        } catch (Exception ignored) {
            return null;
        }
    }
}