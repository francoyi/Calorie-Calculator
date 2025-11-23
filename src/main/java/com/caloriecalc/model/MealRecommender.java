package com.caloriecalc.model;

import java.util.ArrayList;
import java.util.List;

public class MealRecommender implements Recommender {
    private final List<FoodItem> foodItems;
    private final List<Recommendation> recommendationList;
    private final int goal;

    /**
     * Computes the recommendationList, for queries
     *
     * @param foodItems list of available food items
     * @param maxKCal   upper bound on calorie queries
     */
    public MealRecommender(List<FoodItem> foodItems, double maxKCal) {
        /*
            TODO: this recommendation algorithm needs some work.
            it really likes recommending a ton of really low calorie items
        */
        this.foodItems = foodItems;
        this.goal = (int) maxKCal;
        this.recommendationList = new ArrayList<>(this.goal + 1);

        for (int i = 0; i <= this.goal; i++) {
            this.recommendationList.add(new Recommendation(new ArrayList<>()));
        }

        for (int i = 0; i <= maxKCal; i++) {
            for (FoodItem foodItem : foodItems) {
                int nxt = (int) (recommendationList.get(i).getCalories() + foodItem.kcalPerServing());
                if (nxt <= this.goal) {
                    recommendationList.set(nxt, (new Recommendation(recommendationList.get(i).getFoodItems())));
                    recommendationList.get(nxt).addFoodItem(foodItem);
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
