package Test.usecase.Visualize;

import com.caloriecalc.model.*;
import com.caloriecalc.port.FoodLogRepository;
import com.caloriecalc.port.visualize.*;
import com.caloriecalc.service.VisualizeNutritionInteractor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VisualizeInteractorTest {

    @Test
    void testMacroCalculationAccuracy() {
        FoodLogRepository mockRepo = new FoodLogRepository() {
            @Override
            public DailyLog getDay(LocalDate date) {
                DailyLog log = new DailyLog(date);
                List<Meal> meals = new ArrayList<>();

                Meal meal = new Meal("meal1", date, "Lunch");
                List<MealEntry> entries = new ArrayList<>();

                NutritionValues nutrition = new NutritionValues(200.0, 10.0, 5.0, 20.0, 0.0, 0.0, 0.0);

                MealEntry entry = new MealEntry(
                        "Test Chicken",
                        "100g chicken",
                        new Serving(100.0, "g"),
                        200.0,
                        "API",
                        null,
                        nutrition
                );

                entries.add(entry);
                meal.setEntries(entries);
                meals.add(meal);
                log.setMeals(meals);

                return log;
            }

            @Override
            public void saveDay(DailyLog day) {
            }
        };

        class TestPresenter implements VisualizeNutritionOutputBoundary {
            VisualizeNutritionOutputData receivedData;
            String receivedError;

            @Override
            public void present(VisualizeNutritionOutputData output) {
                this.receivedData = output;
            }

            @Override
            public void prepareFailView(String error) {
                this.receivedError = error;
            }
        }

        TestPresenter spyPresenter = new TestPresenter();

        VisualizeNutritionInteractor interactor = new VisualizeNutritionInteractor(mockRepo, spyPresenter);
        interactor.execute(new VisualizeNutritionInputData(LocalDate.now()));

        assertNull(spyPresenter.receivedError, "Should not fail for valid data");
        assertNotNull(spyPresenter.receivedData, "Should receive output data");

        assertEquals(10.0, spyPresenter.receivedData.totalProtein(), 0.01);
        assertEquals(5.0, spyPresenter.receivedData.totalFat(), 0.01);
        assertEquals(20.0, spyPresenter.receivedData.totalCarbs(), 0.01);
        assertEquals(200.0, spyPresenter.receivedData.totalCalories(), 0.01);
    }

    @Test
    void testNoDataHandling() {
        FoodLogRepository emptyRepo = new FoodLogRepository() {
            @Override
            public DailyLog getDay(LocalDate date) {
                return null;
            }
            @Override public void saveDay(DailyLog day) {}
        };

        class TestPresenter implements VisualizeNutritionOutputBoundary {
            String error;
            @Override public void present(VisualizeNutritionOutputData output) { fail("Should not succeed"); }
            @Override public void prepareFailView(String error) { this.error = error; }
        }
        TestPresenter spyPresenter = new TestPresenter();

        VisualizeNutritionInteractor interactor = new VisualizeNutritionInteractor(emptyRepo, spyPresenter);
        interactor.execute(new VisualizeNutritionInputData(LocalDate.now()));

        assertNotNull(spyPresenter.error);
        assertTrue(spyPresenter.error.contains("No meal data"));
    }
}