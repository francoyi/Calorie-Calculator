package Test.usecase.UserMetricsStorage;
import com.caloriecalc.entity.ActivityLevel;
import com.caloriecalc.entity.CalDevianceRate;
import com.caloriecalc.entity.UserMetrics;
import com.caloriecalc.infrastructure.repo.JsonUserMetricsRepository;
import org.junit.jupiter.api.io.TempDir;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertThrows;


public class UserMetricsStorageTest {
    @TempDir
    Path tempDir;

    private JsonUserMetricsRepository createRepo() {
        Path file = tempDir.resolve("user_metrics.json");
        return new JsonUserMetricsRepository(file);
    }

    @Test
    public void loadReturnsEmptyWhenFileDoesNotExist() {
        JsonUserMetricsRepository repo = createRepo();

        Optional<UserMetrics> loaded = repo.load();

        assertTrue(loaded.isEmpty());
    }

    @Test
    public void saveAndLoadRoundTrip() {
        JsonUserMetricsRepository repo = createRepo();
        UserMetrics metrics = new UserMetrics(
                20,
                70.0,
                175.0,
                UserMetrics.Sex.MALE,
                ActivityLevel.MEDIUM,
                CalDevianceRate.MAINTAIN_0wk,
                true
        );

        repo.save(metrics);
        Optional<UserMetrics> loaded = repo.load();

        assertTrue(loaded.isPresent());
        assertEquals(metrics, loaded.get());
    }

    @Test
    public void saveNullThrows() {
        JsonUserMetricsRepository repo = createRepo();

        assertThrows(IllegalArgumentException.class, () -> repo.save(null));
    }
}