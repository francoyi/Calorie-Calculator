
package com.caloriecalc.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class Meal {
  private String id;
  private LocalDate date;
  private String label;
  private String notes;
  private List<MealEntry> entries = new ArrayList<>();
  private double totalKcal;
  public Meal(){}
  public Meal(String id, LocalDate date, String label){ this.id=id; this.date=date; this.label=label; }
  public String getId(){ return id; } public void setId(String id){ this.id=id; }
  public LocalDate getDate(){ return date; } public void setDate(LocalDate d){ this.date=d; }
  public String getLabel(){ return label; } public void setLabel(String l){ this.label=l; }
  public String getNotes(){ return notes; } public void setNotes(String n){ this.notes=n; }
  public List<MealEntry> getEntries(){ return entries; } public void setEntries(List<MealEntry> e){ this.entries=e; }
  public double getTotalKcal(){ return totalKcal; } public void setTotalKcal(double k){ this.totalKcal=k; }
}
