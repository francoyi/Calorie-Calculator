package Test.usecase.RecommenderTests;

import com.caloriecalc.model.FoodItem;
import com.caloriecalc.model.MealRecommender;
import com.caloriecalc.model.Recommendation;
import com.caloriecalc.port.FoodLogRepository;
import com.caloriecalc.port.NutritionDataProvider;
import com.caloriecalc.port.UserSettingsRepository;
import com.caloriecalc.service.FoodLogService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FoodLogServiceImpl extends FoodLogService {
    public FoodLogServiceImpl(FoodLogRepository repo, NutritionDataProvider provider, UserSettingsRepository settingsRepo) {
        super(repo, provider, settingsRepo);
    }

    @Override
    public double getDailyGoal() {
        return 2000.0; // Target 2000 kcal
    }

    public static class MealRecommenderTest {

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
}
