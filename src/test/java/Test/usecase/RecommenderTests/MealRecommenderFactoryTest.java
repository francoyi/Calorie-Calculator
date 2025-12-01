package Test.usecase.RecommenderTests;

import com.caloriecalc.factory.MealRecommenderFactory;
import com.caloriecalc.model.FoodItem;
import com.caloriecalc.model.MealRecommender;
import com.caloriecalc.model.Recommender;
import com.caloriecalc.port.FoodLogRepository;
import com.caloriecalc.port.NutritionDataProvider;
import com.caloriecalc.port.UserSettingsRepository;
import com.caloriecalc.repo.JsonFoodLogRepository;
import com.caloriecalc.repo.JsonUserSettingsRepository;
import com.caloriecalc.service.FoodLogService;
import com.caloriecalc.service.OpenFoodFactsClient;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;

public class MealRecommenderFactoryTest {

    private FoodLogService service;
    private MealRecommenderFactory factory;


    @Test
    void testFactoryCreationAndDependencyInjection() {
        FoodLogRepository foodLogRepo = new JsonFoodLogRepository(Path.of("data", "food_log.json"));
        UserSettingsRepository settingsRepo = new JsonUserSettingsRepository(Path.of("data", "user_settings.json"));
        NutritionDataProvider provider = new OpenFoodFactsClient();
        service = new FoodLogServiceImpl(foodLogRepo, provider, settingsRepo);
        factory = new MealRecommenderFactory(service);
        List<FoodItem> testFoods = List.of(new FoodItemImpl("testA", 10), new FoodItemImpl("testB", 20));
        double expectedGoal = service.getDailyGoal();

        Recommender recommender = factory.create(testFoods);

        assertNotNull(recommender, "Recommender should not be null");

        assertInstanceOf(MealRecommender.class, recommender, "Factory should create an instance of MealRecommender");

        MealRecommender concreteRecommender = (MealRecommender) recommender;

        assertEquals(2, concreteRecommender.getFoods().size(), "Recommender should be initialized with 2 foods");
        assertEquals("testA", concreteRecommender.getFoods().get(0).name(), "Food names should match, and be in order");

        assertEquals(expectedGoal, concreteRecommender.getGoal(), "The Recommender should have the DailyGoal from the GoalService");
        assertEquals(2000.0, concreteRecommender.getGoal(), "Check target goal value");
    }

    @Test
    void testFactoryWithEmptyFoodsList() {
        FoodLogRepository foodLogRepo = new JsonFoodLogRepository(Path.of("data", "food_log.json"));
        UserSettingsRepository settingsRepo = new JsonUserSettingsRepository(Path.of("data", "user_settings.json"));
        NutritionDataProvider provider = new OpenFoodFactsClient();
        service = new FoodLogServiceImpl(foodLogRepo, provider, settingsRepo);
        factory = new MealRecommenderFactory(service);

        List<FoodItem> emptyFoods = new ArrayList<>();

        Recommender recommender = factory.create(emptyFoods);

        assertInstanceOf(MealRecommender.class, recommender, "Factory should still create a Recommender even if input list is empty");
        MealRecommender concreteRecommender = (MealRecommender) recommender;
        assertEquals(0, concreteRecommender.getFoods().size(), "Recommender should be initialized with empty list");
    }
}