package Test;

import com.caloriecalc.model.DailyLog;
import com.caloriecalc.model.UserSettings;
import com.caloriecalc.port.FoodLogRepository;
import com.caloriecalc.port.NutritionDataProvider;
import com.caloriecalc.port.UserSettingsRepository;
import com.caloriecalc.service.FoodLogService;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import com.caloriecalc.model.*;
import com.caloriecalc.model.*;


public class FoodLogServiceTest {

    /**
     * Mock implementation of UserSettingsRepository.
     * Stores settings in memory instead of a file.
     */
    static class MockSettingsRepo implements UserSettingsRepository {
        private UserSettings settings = new UserSettings();
        @Override public UserSettings getSettings() { return settings; }
        @Override public void saveSettings(UserSettings s) { this.settings = s; }
    }

    /**
     * Mock implementation of FoodLogRepository.
     * Returns dummy data for the current date and null for other dates.
     */
    static class MockFoodLogRepo implements FoodLogRepository {
        private DailyLog savedLog;

        @Override
        public DailyLog getDay(LocalDate date) {
            if (savedLog != null && savedLog.getDate().equals(date)) {
                return savedLog;
            }

            if (date.equals(LocalDate.now())) {
                DailyLog log = new DailyLog(date);
                log.setTotalKcal(500.0);
                log.setMeals(new ArrayList<>());
                return log;
            }
            return null;
        }

        @Override
        public void saveDay(DailyLog day) {
            this.savedLog = day;
        }

        @Override
        public List<DailyLog> getAllDays() {
            return new ArrayList<>();
        }
    }

    static class MockProvider implements NutritionDataProvider {
        @Override
        public NutritionValues fetchNutritionPer100(String term) {
            if ("apple".equals(term)) {
                return new NutritionValues(52.0, 0.3, 0.2, 14.0, 10.0, 2.4, 1.0);
            }
            if ("water".equals(term)) {
                return new NutritionValues(null, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
            }
            return null;
        }
    }

    /**
     * Test setting a valid daily calorie goal.
     */
    @Test
    public void testSetDailyGoal_Success() {
        MockSettingsRepo mockRepo = new MockSettingsRepo();
        FoodLogService service = new FoodLogService(null, null, mockRepo);

        service.setDailyGoal(2500.0);

        assertEquals(2500.0, service.getDailyGoal(), 0.01);
        assertEquals(2500.0, mockRepo.getSettings().getDailyKcalGoal(), 0.01);
    }

    /**
     * Test that setting a negative goal throws an IllegalArgumentException.
     */
    @Test
    public void testSetDailyGoal_NegativeInput_ThrowsException() {
        FoodLogService service = new FoodLogService(null, null, new MockSettingsRepo());

        assertThrows(IllegalArgumentException.class, () -> service.setDailyGoal(-100));
    }

    /**
     * Test retrieving an existing daily log from the repository.
     */
    @Test
    public void testGetDay_ExistingData() {
        FoodLogService service = new FoodLogService(new MockFoodLogRepo(), null, new MockSettingsRepo());

        DailyLog log = service.getDay(LocalDate.now());

        assertNotNull(log);
        assertEquals(500.0, log.getTotalKcal(), 0.01);
    }

    /**
     * Test that requesting a non-existent day returns a new empty DailyLog object.
     */
    @Test
    public void testGetDay_NoData_ReturnsNewEmptyLog() {
        FoodLogService service = new FoodLogService(new MockFoodLogRepo(), null, new MockSettingsRepo());

        LocalDate yesterday = LocalDate.now().minusDays(1);
        DailyLog log = service.getDay(yesterday);

        assertNotNull(log);
        assertEquals(0.0, log.getTotalKcal(), 0.01);
        assertEquals(yesterday, log.getDate());
    }

    @Test
    public void testMealOperations_FullCoverage() {
        // Setup
        MockFoodLogRepo logRepo = new MockFoodLogRepo();
        MockProvider provider = new MockProvider();
        FoodLogService service = new FoodLogService(logRepo, provider, new MockSettingsRepo());
        LocalDate today = LocalDate.now();

        // 1. Setup Meals
        Meal meal1 = service.newEmptyMeal(today, "Breakfast");
        // We try to set ID, but we will use getter to be safe
        meal1.setId("id-1");

        Meal meal2 = service.newEmptyMeal(today, "Lunch");
        meal2.setId("id-2");

        // 2. Setup Entries
        MealEntry apple = service.resolveEntryFromInput("100g apple");
        MealEntry water = service.resolveEntryFromInput("100g water"); // Null Calories

        List<MealEntry> list1 = new ArrayList<>();
        list1.add(apple);
        list1.add(water);
        meal1.setEntries(list1);

        // 3. Save Meal 1
        service.saveMeal(today, meal1);
        assertEquals(1, logRepo.getDay(today).getMeals().size());

        // 4. Save Meal 2
        service.saveMeal(today, meal2);
        assertEquals(2, logRepo.getDay(today).getMeals().size());

        // 5. Update Meal 1
        meal1.setLabel("Updated");
        service.saveMeal(today, meal1);
        assertEquals("Updated", logRepo.getDay(today).getMeals().get(0).getLabel());

        // 6. Delete Meal 1 (Critical Fix: Use getter!)
        service.deleteMeal(today, meal1.getId());

        // Assert: Should be 1 left
        assertEquals(1, logRepo.getDay(today).getMeals().size());
        // Assert: The remaining one should be Meal 2
        assertEquals(meal2.getId(), logRepo.getDay(today).getMeals().get(0).getId());

        // 7. Delete Meal 2
        service.deleteMeal(today, meal2.getId());
        assertTrue(logRepo.getDay(today).getMeals().isEmpty());

        // 8. Delete invalid
        service.deleteMeal(today.minusDays(100), "fakeId");
    }

    @Test
    public void testGettersAndConstructors_FullCoverage() {
        MockFoodLogRepo repo = new MockFoodLogRepo();
        MockSettingsRepo settingsRepo = new MockSettingsRepo();
        MockProvider provider = new MockProvider();

        FoodLogService service = new FoodLogService(repo, provider, settingsRepo);

        assertNotNull(service.getRepository());
        assertNotNull(service.getSettingsRepo());
        assertNotNull(service.getSettings());

        service.setDailyGoal(1500.0);
        assertEquals(1500.0, service.getDailyGoal(), 0.01);
    }

    @Test
    public void testGetDay_Null_CreatesFullyInitializedLog() {
        MockFoodLogRepo repo = new MockFoodLogRepo();
        FoodLogService service = new FoodLogService(repo, new MockProvider(), new MockSettingsRepo());

        DailyLog log = service.getDay(LocalDate.of(2000, 1, 1));

        assertNotNull(log);
        assertNotNull(log.getMeals());
        assertEquals(0.0, log.getTotalKcal());
    }

    @Test
    public void testDeleteMeal_DayNotFound() {
        MockFoodLogRepo repo = new MockFoodLogRepo();
        FoodLogService service = new FoodLogService(repo, new MockProvider(), new MockSettingsRepo());

        service.deleteMeal(LocalDate.of(1990, 1, 1), "some-id");
    }

    @Test
    public void testSaveMealWithNullCalories() {
        MockFoodLogRepo repo = new MockFoodLogRepo();
        FoodLogService service = new FoodLogService(repo, null, new MockSettingsRepo());
        LocalDate today = LocalDate.now();

        Meal meal = service.newEmptyMeal(today, "Ghost Meal");
        MealEntry nullEntry = new MealEntry("Ghost", "Ghost", new Serving(1,"g"), null, "Manual", null, null);
        List<MealEntry> entries = new ArrayList<>();
        entries.add(nullEntry);
        meal.setEntries(entries);

        service.saveMeal(today, meal);

        assertNotNull(repo.getDay(today));
    }

    /**
     * Tests edge cases for input parsing (missing units, missing amounts, whitespace).
     * Ensures the parser defaults to sensible values (100.0, "g").
     */
    @Test
    public void testInputParsingEdgeCases() {
        MockFoodLogRepo logRepo = new MockFoodLogRepo();
        MockProvider provider = new MockProvider();
        FoodLogService service = new FoodLogService(logRepo, provider, new MockSettingsRepo());

        // 1. Missing amount & unit -> default 100g
        MealEntry e1 = service.resolveEntryFromInput("apple");
        assertEquals(100.0, e1.serving().amount());
        assertEquals("g", e1.serving().unit());

        // 2. Missing name (only unit) -> parses unit correctly
        MealEntry e2 = service.resolveEntryFromInput("100ml milk");
        assertEquals("ml", e2.serving().unit());

        // 3. Whitespace handling
        MealEntry e3 = service.resolveEntryFromInput("   apple   ");
        assertEquals("apple", e3.name());

        // 4. Save empty meal list (Logic safety check)
        Meal emptyMeal = service.newEmptyMeal(LocalDate.now(), "Empty");
        emptyMeal.setEntries(new ArrayList<>());
        service.saveMeal(LocalDate.now(), emptyMeal);
    }

    @Test
    public void testGoalAndGetters_FullCoverage() {
        MockSettingsRepo settingsRepo = new MockSettingsRepo();
        FoodLogService service = new FoodLogService(new MockFoodLogRepo(), new MockProvider(), settingsRepo);

        service.setDailyGoal(2000.0);
        assertEquals(2000.0, service.getDailyGoal(), 0.1); // 覆盖 getDailyGoal

        assertThrows(IllegalArgumentException.class, () -> service.setDailyGoal(-100.0));

        assertNotNull(service.getRepository());
        assertNotNull(service.getSettingsRepo());
        assertNotNull(service.getSettings());
    }

    /**
     * Test saving a meal to a BRAND NEW date (e.g. tomorrow).
     * This triggers the "if (day == null)" logic in saveMeal,
     * creating a new DailyLog from scratch.
     */
    @Test
    public void testSaveMeal_NewDay_CreatesLog() {
        // Setup
        MockFoodLogRepo repo = new MockFoodLogRepo();
        FoodLogService service = new FoodLogService(repo, new MockProvider(), new MockSettingsRepo());

        // Use "Tomorrow" because our MockRepo only returns data for "Today".
        // For tomorrow, it returns null, forcing the Service to create a new Log.
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        Meal meal = service.newEmptyMeal(tomorrow, "Future Meal");
        // Add a dummy entry to ensure loop logic runs too
        MealEntry entry = new MealEntry("Apple", "apple", new Serving(100, "g"), 52.0, "API", null, null);
        List<MealEntry> list = new ArrayList<>();
        list.add(entry);
        meal.setEntries(list);

        // Execute Save
        service.saveMeal(tomorrow, meal);

        // Verify
        DailyLog saved = repo.getDay(tomorrow);
        assertNotNull(saved, "Should have created and saved a new DailyLog");
        assertEquals(tomorrow, saved.getDate());
        assertEquals(1, saved.getMeals().size());
    }
}