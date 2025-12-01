package com.caloriecalc.service;

import com.caloriecalc.entity.MealRecommendationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class MealRecommendationServiceTest {
    @Test
    void testEmptyDefaults() {
        MealRecommendationService mrs = new MealRecommendationServiceImpl();
        Assertions.assertEquals(Collections.emptyList(), mrs.recommendMealEntries());
    }
}
