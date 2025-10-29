
package com.caloriecalc.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class DailyLog {
  private LocalDate date;
  private List<Meal> meals = new ArrayList<>();
  private double totalKcal;
  public DailyLog(){} public DailyLog(LocalDate date){ this.date=date; }
  public LocalDate getDate(){ return date; } public void setDate(LocalDate d){ this.date=d; }
  public List<Meal> getMeals(){ return meals; } public void setMeals(List<Meal> ms){ this.meals=ms; }
  public double getTotalKcal(){ return totalKcal; } public void setTotalKcal(double k){ this.totalKcal=k; }
}
