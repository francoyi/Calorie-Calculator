package com.caloriecalc.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.Assert.*;

class MealRecommendationServiceTest {
    @Test
    void testEmptyDefaults() {
        MealRecommendationService mrs = new MealRecommendationServiceImpl();
        Assertions.assertEquals(Collections.emptyList(), mrs.recommendMealEntries());
    }
}
