package com.caloriecalc.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MealRecommender implements Recommender {
    private final List<FoodItem> foodItems;
    private final List<Recommendation> recommendationList;
    private final int goal;

    /**
     * Computes the recommendationList, for queries. Less likely to recommend
     * the same food multiple times.
     *
     * @param foodItems list of available food items
     * @param maxKCal   upper bound on calorie queries
     */
    public MealRecommender(List<FoodItem> foodItems, double maxKCal) {
        this.foodItems = foodItems;
        this.goal = (int) maxKCal;
        this.recommendationList = new ArrayList<>(this.goal + 1);

        for (int i = 0; i <= this.goal; i++) {
            this.recommendationList.add(new Recommendation(new ArrayList<>()));
        }

        for (int i = 0; i <= this.goal; i++) {
            if (recommendationList.get(i).getFoodItems().isEmpty() && i != 0) {
                continue;
            }

            for (FoodItem foodItem : foodItems) {
                for (int count = 1; count <= 3; count++) {
                    int caloriesToAdd = (int) (foodItem.kcalPerServing() * count);
                    int nxt = i + caloriesToAdd;

                    if (nxt <= this.goal) {
                        int currentCount = 0;
                        if (i != 0) {
                            currentCount = Collections.frequency(recommendationList.get(i).getFoodItems(), foodItem);
                        }

                        if (currentCount + count <= 3) {
                            Recommendation newRecommendation = new Recommendation(recommendationList.get(i).getFoodItems());
                            for (int k = 0; k < count; k++) {
                                newRecommendation.addFoodItem(foodItem);
                            }
                            recommendationList.set(nxt, newRecommendation);
                        }
                    }
                }
            }
        }
    }

    /**
     * Give top n recommendations of food combos under maxCal calories
     *
     * @param n maximum number of recommendations
     * @return a list of Recommendation objects, representing top n food combos
     */
    public List<Recommendation> getTopFoodRecommendations(int n) {
        List<Recommendation> result = new ArrayList<>();
        for (int i = goal; i >= 0 && n > 0; i--) {
            if (!recommendationList.get(i).getFoodItems().isEmpty()) {
                result.add(recommendationList.get(i));
                n--;
            }
        }
        return result;
    }

    public int getGoal() {
        return goal;
    }

    public List<FoodItem> getFoods() {
        return foodItems;
    }
}
