
package com.caloriecalc.model;
public class APIFoodItem implements FoodItem {
  private final String name;
  private final NutritionValues per100g;
  private final double servingAmount;
  public APIFoodItem(String name, NutritionValues per100g, double servingAmount){
    this.name = name; this.per100g = per100g; this.servingAmount = servingAmount;
  }
  @Override public String getName(){ return name; }
  @Override public double getKcalPerServing(){
    if (per100g == null || per100g.energyKcal() == null) return Double.NaN;
    return per100g.energyKcal() * (servingAmount / 100.0);
  }
  @Override public String getSource(){ return "OpenFoodFacts"; }
  public NutritionValues per100g(){ return per100g; }
  public double servingAmount(){ return servingAmount; }
}
