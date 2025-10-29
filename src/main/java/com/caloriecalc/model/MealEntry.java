
package com.caloriecalc.model;
import java.time.ZonedDateTime;
/** Persisted entry — we keep primitive fields for stable JSON, while FoodItem is used in service logic. */
public record MealEntry(
  String name,
  String input,
  Serving serving,
  Double kcalForServing,
  String source,
  ZonedDateTime fetchedAt,
  NutritionValues nutritionPer100g
) { }
