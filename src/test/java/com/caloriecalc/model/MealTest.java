package com.caloriecalc.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

class MealTest {
    @Test
    void testDefaults() {
        Meal meal = new Meal();
        Assertions.assertEquals("", meal.getLabel());
    }
    @Test
    void testSetDate() {
        Meal meal = new Meal();
        LocalDate expectedDate = LocalDate.now();
        meal.setDate(expectedDate);
        assertEquals("Unexpected date", expectedDate, meal.getDate());
        assertNotNull("Date should not be null", meal.getDate());
    }

    @Test
    void testSetNotes() {
        Meal meal = new Meal();
        assertEquals("Notes should be empty", meal.getNotes(), "");

        String note = "Notes here";
        meal.setNotes(note);
        assertEquals("Notes should update", meal.getNotes(), note);

    }
}
