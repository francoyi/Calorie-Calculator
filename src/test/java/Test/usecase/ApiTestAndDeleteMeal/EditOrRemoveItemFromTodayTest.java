package Test.usecase.ApiTestAndDeleteMeal;

import com.caloriecalc.model.DailyLog;
import com.caloriecalc.model.Meal;
import com.caloriecalc.model.MealEntry;
import com.caloriecalc.model.NutritionValues;
import com.caloriecalc.model.Serving;
import com.caloriecalc.model.UserSettings;
import com.caloriecalc.port.FoodLogRepository;
import com.caloriecalc.port.NutritionDataProvider;
import com.caloriecalc.port.UserSettingsRepository;
import com.caloriecalc.service.FoodLogService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Self-test for Use Case 6: Edit or Remove an Item from Today.
 *
 * This file focuses on the domain/service layer:
 *  - saving a meal for today
 *  - editing the same meal (same id) and ensuring today's total kcal is updated
 *  - deleting the meal and ensuring it is removed and totals are recalculated
 */
public class EditOrRemoveItemFromTodayTest {

    // --- Simple in-memory test doubles ---

    private static class InMemoryFoodLogRepo implements FoodLogRepository {
        private final Map<LocalDate, DailyLog> store = new HashMap<>();

        @Override
        public DailyLog getDay(LocalDate date) {
            return store.get(date);
        }

        @Override
        public void saveDay(DailyLog day) {
            store.put(day.getDate(), day);
        }
    }

    private static class DummySettingsRepo implements UserSettingsRepository {
        private UserSettings settings = new UserSettings();

        @Override
        public UserSettings getSettings() {
            return settings;
        }

        @Override
        public void saveSettings(UserSettings settings) {
            this.settings = settings;
        }
    }

    private static class DummyNutritionProvider implements NutritionDataProvider {
        @Override
        public NutritionValues fetchNutritionPer100(String term) {
            // Not needed for this use case: we directly provide kcal in MealEntry
            return null;
        }
    }

    /**
     * UC6 – Edit flow:
     *  User edits an existing meal (e.g., change amount of banana) and the system
     *  must recalculate BOTH the meal's total and today's DailyLog total.
     */
    @Test
    public void editExistingMeal_recalculatesTodaysTotal() {
        InMemoryFoodLogRepo repo = new InMemoryFoodLogRepo();
        FoodLogService service = new FoodLogService(
                repo,
                new DummyNutritionProvider(),
                new DummySettingsRepo()
        );

        LocalDate today = LocalDate.now();

        // Original meal: total = 90 + 120 = 210 kcal
        Meal breakfast = new Meal("meal-1", today, "Breakfast");
        List<MealEntry> originalEntries = new ArrayList<>();
        originalEntries.add(new MealEntry(
                "banana",
                "banana 100g",
                new Serving(100.0, "g"),
                90.0,
                "test",
                null,
                null
        ));
        originalEntries.add(new MealEntry(
                "milk",
                "milk 200ml",
                new Serving(200.0, "ml"),
                120.0,
                "test",
                null,
                null
        ));
        breakfast.setEntries(originalEntries);

        service.saveMeal(today, breakfast);

        DailyLog dayAfterFirstSave = service.getDay(today);
        assertEquals(210.0, dayAfterFirstSave.getTotalKcal(), 0.01);
        assertEquals(1, dayAfterFirstSave.getMeals().size());
        assertEquals("meal-1", dayAfterFirstSave.getMeals().get(0).getId());

        // Edited meal (same id): banana increased to 150g -> 135 kcal
        // New total should be 135 + 120 = 255 kcal
        Meal editedBreakfast = new Meal("meal-1", today, "Breakfast");
        List<MealEntry> editedEntries = new ArrayList<>();
        editedEntries.add(new MealEntry(
                "banana",
                "banana 150g",
                new Serving(150.0, "g"),
                135.0,
                "test",
                null,
                null
        ));
        editedEntries.add(new MealEntry(
                "milk",
                "milk 200ml",
                new Serving(200.0, "ml"),
                120.0,
                "test",
                null,
                null
        ));
        editedBreakfast.setEntries(editedEntries);

        // This should REPLACE the old meal (same id) and recalc day total.
        service.saveMeal(today, editedBreakfast);

        DailyLog dayAfterEdit = service.getDay(today);
        assertEquals(255.0, dayAfterEdit.getTotalKcal(), 0.01);
        assertEquals(1, dayAfterEdit.getMeals().size());
        Meal stored = dayAfterEdit.getMeals().get(0);
        assertEquals("meal-1", stored.getId());
        assertEquals("Breakfast", stored.getLabel());
    }

    /**
     * UC6 – Remove flow:
     *  User deletes one meal from today's list; the system removes it and
     *  updates today's total kcal accordingly.
     */
    @Test
    public void deleteMeal_removesItAndUpdatesTodayTotal() {
        InMemoryFoodLogRepo repo = new InMemoryFoodLogRepo();
        FoodLogService service = new FoodLogService(
                repo,
                new DummyNutritionProvider(),
                new DummySettingsRepo()
        );

        LocalDate today = LocalDate.now();

        // Breakfast: 100 kcal
        Meal breakfast = new Meal("meal-1", today, "Breakfast");
        breakfast.setEntries(List.of(
                new MealEntry(
                        "toast",
                        "toast 100g",
                        new Serving(100.0, "g"),
                        100.0,
                        "test",
                        null,
                        null
                )
        ));

        // Lunch: 300 kcal
        Meal lunch = new Meal("meal-2", today, "Lunch");
        lunch.setEntries(List.of(
                new MealEntry(
                        "rice",
                        "rice 200g",
                        new Serving(200.0, "g"),
                        300.0,
                        "test",
                        null,
                        null
                )
        ));

        service.saveMeal(today, breakfast);
        service.saveMeal(today, lunch);

        DailyLog beforeDelete = service.getDay(today);
        assertEquals(400.0, beforeDelete.getTotalKcal(), 0.01);
        assertEquals(2, beforeDelete.getMeals().size());

        // Delete one "item" (one meal) from today's list
        service.deleteMeal(today, "meal-1");

        DailyLog afterDelete = service.getDay(today);
        assertEquals(300.0, afterDelete.getTotalKcal(), 0.01);
        assertEquals(1, afterDelete.getMeals().size());
        assertEquals("meal-2", afterDelete.getMeals().get(0).getId());
    }
}
