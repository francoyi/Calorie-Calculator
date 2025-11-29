package com.caloriecalc.model;

public record NutritionValues(Double energyKcal, Double proteinG, Double fatG, Double carbG,
                              Double sugarG, Double fiberG, Double sodiumMg) {
    public NutritionValues scale(double factor) {
        Double e = energyKcal == null ? null : energyKcal * factor;
        Double p = proteinG == null ? null : proteinG * factor;
        Double f = fatG == null ? null : fatG * factor;
        Double c = carbG == null ? null : carbG * factor;
        Double s = sugarG == null ? null : sugarG * factor;
        Double fi = fiberG == null ? null : fiberG * factor;
        Double so = sodiumMg == null ? null : sodiumMg * factor;
        return new NutritionValues(e, p, f, c, s, fi, so);
    }

    public Double kcalPer100g() {
        return energyKcal == null ? null : energyKcal;
    }

}
