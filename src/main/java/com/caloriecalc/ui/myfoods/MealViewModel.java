package com.caloriecalc.ui.myfoods;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * View Model for the "Add MyFood to Meal" use case.
 *
 * Stores the temporary UI state produced by the presenter.
 * MealDialog will observe this model and update the UI.
 */
public class MealViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private State state;

    /**
     * Immutable DTO representing a single MealRow to be added.
     */
    public static class State {
        private final String itemName;
        private final double amount;
        private final String unit;
        private final Double kcal;

        public State(String itemName, double amount, String unit, Double kcal) {
            this.itemName = itemName;
            this.amount = amount;
            this.unit = unit;
            this.kcal = kcal;
        }

        public String itemName() { return itemName; }
        public double amount() { return amount; }
        public String unit() { return unit; }
        public Double kcal() { return kcal; }
    }

    public void setState(State newState) {
        this.state = newState;
        support.firePropertyChange("mealState", null, newState);
    }

    public State getState() {
        return state;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
