package Test;


import com.caloriecalc.factory.MealRecommenderFactory;
import com.caloriecalc.factory.RecommenderFactory;
import com.caloriecalc.model.MealEntry;
import com.caloriecalc.port.FoodLogRepository;
import com.caloriecalc.port.NutritionDataProvider;
import com.caloriecalc.port.UserSettingsRepository;
import com.caloriecalc.port.tdee.NutritionDataProviderImpl;
import com.caloriecalc.repo.JsonFoodLogRepository;
import com.caloriecalc.repo.JsonUserSettingsRepository;
import com.caloriecalc.service.DefaultMealRecommendationService;
import com.caloriecalc.service.FoodLogService;
import com.caloriecalc.service.MealRecommendationService;
import com.caloriecalc.service.OpenFoodFactsClient;
import com.caloriecalc.testutil.FoodLogServiceImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.List;

class DefaultMealRecommendationServiceTest {

    private DefaultMealRecommendationService defaultMealRecommendationService;
    private FoodLogService service;
    private MealRecommenderFactory factory;

    @Test
    void TestRecommendationSimple() {
        FoodLogRepository foodLogRepo = new JsonFoodLogRepository(Path.of("data", "food_log.json"));
        UserSettingsRepository settingsRepo = new JsonUserSettingsRepository(Path.of("data", "user_settings.json"));
        NutritionDataProvider provider = new NutritionDataProviderImpl();
        service = new FoodLogServiceImpl(foodLogRepo, provider, settingsRepo);
        factory = new MealRecommenderFactory(service);
        this.defaultMealRecommendationService = new DefaultMealRecommendationService(provider, factory);

        List<MealEntry> mealEntries = defaultMealRecommendationService.recommendMealEntries();

        assertNotNull(mealEntries, "mealEntries should not be null");
    }
}
