package Test.usecase.History;

import com.caloriecalc.model.DailyLog;
import com.caloriecalc.model.UserSettings;
import com.caloriecalc.port.FoodLogRepository;
import com.caloriecalc.port.UserSettingsRepository;
import com.caloriecalc.port.history.VisualizeHistoryInputData;
import com.caloriecalc.port.history.VisualizeHistoryOutputBoundary;
import com.caloriecalc.port.history.VisualizeHistoryOutputData;
import com.caloriecalc.service.VisualizeHistoryInteractor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VisualizeHistoryInteractorTest {

    @Test
    void testHistoryAggregation() {
        FoodLogRepository mockRepo = new FoodLogRepository() {
            @Override
            public List<DailyLog> getAllDays() {
                List<DailyLog> logs = new ArrayList<>();

                DailyLog d1 = new DailyLog(LocalDate.of(2025, 11, 27));
                d1.setTotalKcal(2000.0);
                logs.add(d1);

                DailyLog d2 = new DailyLog(LocalDate.of(2025, 11, 26));
                d2.setTotalKcal(1500.0);
                logs.add(d2);

                DailyLog d3 = new DailyLog(LocalDate.of(2025, 11, 25));
                d3.setTotalKcal(0.0);
                logs.add(d3);

                return logs;
            }

            @Override public DailyLog getDay(LocalDate date) { return null; }
            @Override public void saveDay(DailyLog day) {}
        };

        UserSettingsRepository mockSettings = new UserSettingsRepository() {
            @Override
            public UserSettings getSettings() {
                return new UserSettings(2500.0);
            }
            @Override public void saveSettings(UserSettings settings) {}
        };

        class TestPresenter implements VisualizeHistoryOutputBoundary {
            VisualizeHistoryOutputData receivedData;

            @Override
            public void present(VisualizeHistoryOutputData output) {
                this.receivedData = output;
            }

            @Override
            public void prepareFailView(String error) {
                fail("Should not fail: " + error);
            }
        }
        TestPresenter presenter = new TestPresenter();

        VisualizeHistoryInteractor interactor = new VisualizeHistoryInteractor(mockRepo, mockSettings, presenter);
        interactor.execute(new VisualizeHistoryInputData());

        assertNotNull(presenter.receivedData);
        assertEquals(2500.0, presenter.receivedData.calorieGoal());

        assertEquals(2, presenter.receivedData.caloriesPerDay().size());

        List<LocalDate> dates = new ArrayList<>(presenter.receivedData.caloriesPerDay().keySet());
        assertEquals(LocalDate.of(2025, 11, 26), dates.get(0));
        assertEquals(LocalDate.of(2025, 11, 27), dates.get(1));

        assertEquals(1500.0, presenter.receivedData.caloriesPerDay().get(dates.get(0)));
        assertEquals(2000.0, presenter.receivedData.caloriesPerDay().get(dates.get(1)));
    }

    @Test
    void testNoHistoryData_ShouldFail() {
        FoodLogRepository emptyRepo = new FoodLogRepository() {
            @Override
            public List<DailyLog> getAllDays() {
                return new ArrayList<>();
            }
            @Override public DailyLog getDay(LocalDate date) { return null; }
            @Override public void saveDay(DailyLog day) {}
        };

        UserSettingsRepository mockSettings = new UserSettingsRepository() {
            @Override public UserSettings getSettings() { return new UserSettings(2000.0); }
            @Override public void saveSettings(UserSettings settings) {}
        };

        class TestPresenter implements VisualizeHistoryOutputBoundary {
            String error;
            @Override public void present(VisualizeHistoryOutputData output) {
                fail("Should not succeed when no data exists");
            }
            @Override public void prepareFailView(String error) {
                this.error = error;
            }
        }
        TestPresenter presenter = new TestPresenter();

        VisualizeHistoryInteractor interactor = new VisualizeHistoryInteractor(emptyRepo, mockSettings, presenter);
        interactor.execute(new VisualizeHistoryInputData());

        assertNotNull(presenter.error);
        assertEquals("No history data found.", presenter.error);
    }
}