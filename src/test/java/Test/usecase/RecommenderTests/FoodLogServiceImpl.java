package Test.usecase.RecommenderTests;

import com.caloriecalc.usecase.foodlog.FoodLogRepository;
import com.caloriecalc.usecase.searchfood.NutritionDataProvider;
import com.caloriecalc.usecase.usersettings.UserSettingsRepository;
import com.caloriecalc.usecase.foodcalorielookup.FoodLogService;
import com.caloriecalc.usecase.myfoods.savetomyfood.InMemoryMyFoodRepository;
public class FoodLogServiceImpl extends FoodLogService {
    public FoodLogServiceImpl(FoodLogRepository repo, NutritionDataProvider provider, UserSettingsRepository settingsRepo) {
        super(repo, provider, settingsRepo, new InMemoryMyFoodRepository());
    }

    @Override
    public double getDailyGoal() {
        return 2000.0; // Target 2000 kcal
    }
}
