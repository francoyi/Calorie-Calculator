package Test;

import com.caloriecalc.model.DailyLog;
import com.caloriecalc.model.UserSettings;
import com.caloriecalc.port.FoodLogRepository;
import com.caloriecalc.port.UserSettingsRepository;
import com.caloriecalc.service.FoodLogService;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

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
        @Override
        public DailyLog getDay(LocalDate date) {
            if (date.equals(LocalDate.now())) {
                DailyLog log = new DailyLog(date);
                log.setTotalKcal(500.0);
                log.setMeals(new ArrayList<>());
                return log;
            }
            return null;
        }
        @Override public void saveDay(DailyLog day) {}
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
}