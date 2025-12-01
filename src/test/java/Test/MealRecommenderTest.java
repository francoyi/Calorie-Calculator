package Test;

import com.caloriecalc.FoodItemImpl.FoodItemImpl;
import com.caloriecalc.entity.FoodItem;
import com.caloriecalc.entity.MealRecommender;
import com.caloriecalc.entity.Recommendation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MealRecommenderTest {

    @Test
    /**
     * Test that DataTypes.sum returns the correct value for
     * the sum from 1 to 1 million.
     */
    public void GetTop10Test() {
            FoodItemImpl a = new FoodItemImpl("a", 700);
            FoodItemImpl b = new FoodItemImpl("b", 1400);
            FoodItemImpl c = new FoodItemImpl("c", 200);

            List<FoodItem> foodItems = new ArrayList<>();
            foodItems.add(a);
            foodItems.add(b);
            foodItems.add(c);

            boolean flag = true;

            MealRecommender fr = new MealRecommender(foodItems, 2900);
            List<Recommendation> top = fr.getTopFoodRecommendations(10);
            for (int i = 0; i < top.size(); i++) {
                System.out.println("#" + Integer.toString(i + 1) + " recommendation:");
                System.out.println(top.get(i));
                if (top.get(i).getCalories() > fr.getGoal()) {
                    flag = false;
                }
            }

        assertTrue(flag, "Each sum should have kcal sum lesser than goal.");
    }
}