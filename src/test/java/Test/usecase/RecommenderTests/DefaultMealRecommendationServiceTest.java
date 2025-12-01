package Test.usecase.RecommenderTests;


import com.caloriecalc.entity.MealEntry;
import com.caloriecalc.entity.MealRecommenderFactory;
import com.caloriecalc.infrastructure.repo.JsonFoodLogRepository;
import com.caloriecalc.infrastructure.repo.JsonUserSettingsRepository;
import com.caloriecalc.usecase.foodcalorielookup.FoodLogService;
import com.caloriecalc.usecase.foodlog.FoodLogRepository;
import com.caloriecalc.usecase.mealrecommendation.DefaultMealRecommendationService;
import com.caloriecalc.usecase.searchfood.NutritionDataProvider;
import com.caloriecalc.usecase.usersettings.UserSettingsRepository;
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
