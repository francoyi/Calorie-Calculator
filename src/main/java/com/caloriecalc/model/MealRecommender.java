package com.caloriecalc.model;

import java.util.ArrayList;
import java.util.List;

public class MealRecommender {
    List<FoodItem> foodItems;
    List<Recommendation> recommendationList;
    int maxKCal;

    /**
     * Computes the recommendationList, for queries
     * @param foodItems list of available food items
     * @param maxKCal upper bound on calorie queries
     */
    public MealRecommender(List<FoodItem> foodItems, double maxKCal) {
        /*
            TODO: this recommendation algorithm needs some work.
            it really likes recommending a ton of really low calorie items
        */
        this.foodItems = foodItems;
        this.maxKCal = (int) maxKCal;
        this.recommendationList = new ArrayList<>(this.maxKCal + 1);

        for (int i = 0; i <= this.maxKCal; i++) {
            this.recommendationList.add(new Recommendation(new ArrayList<>()));
        }

        for (int i = 0; i <= maxKCal; i++) {
            for (FoodItem foodItem : foodItems) {
                int nxt = (int) (recommendationList.get(i).getCalories() + foodItem.getKcalPerServing());
                if (nxt <= this.maxKCal) {
                    recommendationList.set(nxt, (new Recommendation(recommendationList.get(i).getFoodItems())));
                    recommendationList.get(nxt).addFoodItem(foodItem);
                }
            }
        }
    }

    /**
     * Give top n recommendations of food combos under maxCal calories
     * @param n maximum number of recommendations
     * @return a list of Recommendation objects, representing top n food combos
     */
    public List<Recommendation> getTopFoodRecommendations(int n) {
        List<Recommendation> topFoodRecommendations = new ArrayList<>();
        for (int i = maxKCal; i >= 0 && n > 0 ; i--) {
            if (!recommendationList.get(i).getFoodItems().isEmpty()) {
                topFoodRecommendations.add(recommendationList.get(i));
                n--;
            }
        }
        return topFoodRecommendations;
    }

    public int getMaxKCal() {
        return maxKCal;
    }
}
