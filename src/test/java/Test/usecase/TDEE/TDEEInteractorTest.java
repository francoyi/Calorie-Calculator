package Test.usecase.TDEE;

import com.caloriecalc.model.ActivityLevel;
import com.caloriecalc.model.CalDevianceRate;
import com.caloriecalc.model.UserMetrics;
import com.caloriecalc.port.tdee.BMRFormula;
import com.caloriecalc.port.tdee.CalculateTDEEInputData;
import com.caloriecalc.port.tdee.CalculateTDEEOutputBoundary;
import com.caloriecalc.port.tdee.CalculateTDEEOutputData;
import com.caloriecalc.service.CalculateTDEEInteractor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TDEEInteractorTest {
//2682.7
    @Test
    public void failureAgeNotAbove18() {
        BMRFormula bmrFormula = new BMRFormula() {
            @Override
            public double computeBmr(UserMetrics userMetrics) {
                fail("BMR should not be computed when age is below 18");
                return 0;
            }

            @Override
            public String name() {
                return "TestBMR";
            }
        };

        class TestPresenter implements CalculateTDEEOutputBoundary {
            String validationErrorMessage = null;
            CalculateTDEEOutputData outputData = null;

            @Override
            public void present(CalculateTDEEOutputData data) {
                this.outputData = data;
            }

            @Override
            public void presentValidationError(String message) {
                this.validationErrorMessage = message;
            }
        }

        TestPresenter presenter = new TestPresenter();
        CalculateTDEEInteractor interactor = new CalculateTDEEInteractor(bmrFormula, presenter);

        CalculateTDEEInputData input = new CalculateTDEEInputData(
                        12,
                        70.0,
                        175.0,
                true,
                UserMetrics.Sex.MALE,
                ActivityLevel.MEDIUM,
                CalDevianceRate.MAINTAIN_0wk
                );


        interactor.execute(input);

        assertEquals("Age must be 18 or above.", presenter.validationErrorMessage);
        assertNull(presenter.outputData, "Output data should not be produced when validation fails");
    }

    @Test
    public void passAgeExact18() {
    }
}
