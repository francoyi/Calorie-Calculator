package com.caloriecalc.model;

public class UserSettings {
    private double dailyKcalGoal;
    private double weight;
    private double bmr;

    public UserSettings() {
        this.dailyKcalGoal = 2000.0;
    }

    public UserSettings(double goal) {
        this.dailyKcalGoal = goal;
    }

    public double getDailyKcalGoal() {
        return dailyKcalGoal;
    }

    public void setDailyKcalGoal(double goal) {
        this.dailyKcalGoal = goal;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBmr() {
        return bmr;
    }

    public void setBmr(double bmr) {
        this.bmr = bmr;
    }
}
