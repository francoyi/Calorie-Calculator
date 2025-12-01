package com.caloriecalc.entity;

public class UserSettings {
    private double dailyKcalGoal;

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
}
