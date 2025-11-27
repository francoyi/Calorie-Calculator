package Test.usecase.TDEE;

import com.caloriecalc.model.ActivityLevel;
import com.caloriecalc.model.CalDevianceRate;
import com.caloriecalc.model.UserMetrics;
import com.caloriecalc.port.tdee.BMRFormula;
import com.caloriecalc.port.tdee.CalculateTDEEInputData;
import com.caloriecalc.port.tdee.CalculateTDEEOutputBoundary;
import com.caloriecalc.port.tdee.CalculateTDEEOutputData;
import com.caloriecalc.service.CalculateTDEEInteractor;
import com.caloriecalc.service.MifflinStJeorBMR;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TDEEInteractorTest {
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
    public void failureNegWeight() {
        BMRFormula bmrFormula = new BMRFormula() {
            @Override
            public double computeBmr(UserMetrics userMetrics) {
                fail("BMR should not be computed when Weight is negative.");
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
                29,
                -10.0,
                175.0,
                true,
                UserMetrics.Sex.MALE,
                ActivityLevel.MEDIUM,
                CalDevianceRate.MAINTAIN_0wk
        );


        interactor.execute(input);

        assertEquals("Weight must be greater than 0.", presenter.validationErrorMessage);
        assertNull(presenter.outputData, "Output data should not be produced when validation fails");
    }

    @Test
    public void failureNegHeight() {
        BMRFormula bmrFormula = new BMRFormula() {
            @Override
            public double computeBmr(UserMetrics userMetrics) {
                fail("BMR should not be computed when Height is negative.");
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
                29,
                80.0,
                -175.0,
                true,
                UserMetrics.Sex.MALE,
                ActivityLevel.MEDIUM,
                CalDevianceRate.MAINTAIN_0wk
        );


        interactor.execute(input);

        assertEquals("Height must be greater than 0.", presenter.validationErrorMessage);
        assertNull(presenter.outputData, "Output data should not be produced when validation fails");
    }

    @Test
    public void passAgeExact18() {
        BMRFormula bmrFormula = new TestBMR1600();

        class TestPresenter implements CalculateTDEEOutputBoundary {
            CalculateTDEEOutputData output;
            String error;

            @Override
            public void present(CalculateTDEEOutputData data) {
                this.output = data;
            }

            @Override
            public void presentValidationError(String message) {
                this.error = message;
            }
        }

        TestPresenter presenter = new TestPresenter();
        CalculateTDEEInteractor interactor = new CalculateTDEEInteractor(bmrFormula, presenter);

        CalculateTDEEInputData input = new CalculateTDEEInputData(
                        18,
                        70.0,
                        175.0,
                true,
                UserMetrics.Sex.MALE,
                ActivityLevel.MEDIUM,
                CalDevianceRate.MAINTAIN_0wk
                );

        interactor.execute(input);

        assertNull(presenter.error);
        assertNotNull(presenter.output);
        assertEquals(1600, presenter.output.bmr());
        assertEquals(1600 * ActivityLevel.MEDIUM.multiplier + CalDevianceRate.MAINTAIN_0wk.devianceRate,
                presenter.output.tdee());
        assertEquals("Testing BMR - 1600", presenter.output.formulaName());
    }
    
    @Test
    public void passFlooredPositiveOutput() {
        BMRFormula bmrFormula = new MifflinStJeorBMR();
        class TestPresenter implements CalculateTDEEOutputBoundary {
            CalculateTDEEOutputData output;
            String error;

            @Override
            public void present(CalculateTDEEOutputData data) {
                this.output = data;
            }

            @Override
            public void presentValidationError(String message) {
                this.error = message;
            }
        }

        TestPresenter presenter = new TestPresenter();
        CalculateTDEEInteractor interactor = new CalculateTDEEInteractor(bmrFormula, presenter);

        CalculateTDEEInputData input = new CalculateTDEEInputData(
                98,
                7.0,
                17.0,
                true,
                UserMetrics.Sex.MALE,
                ActivityLevel.MEDIUM,
                CalDevianceRate.MAINTAIN_0wk
        );

        interactor.execute(input);

        assertNull(presenter.error);
        assertNotNull(presenter.output);
        assertEquals(0, presenter.output.bmr());
        assertEquals(0 * ActivityLevel.MEDIUM.multiplier + CalDevianceRate.MAINTAIN_0wk.devianceRate,
                presenter.output.tdee());

        assertEquals("Mifflin-St Jeor", presenter.output.formulaName());
    }

    @Test
    public void passFlooredDevRatePositiveOutput() {
        BMRFormula bmrFormula = new MifflinStJeorBMR();
        class TestPresenter implements CalculateTDEEOutputBoundary {
            CalculateTDEEOutputData output;
            String error;

            @Override
            public void present(CalculateTDEEOutputData data) {
                this.output = data;
            }

            @Override
            public void presentValidationError(String message) {
                this.error = message;
            }
        }

        TestPresenter presenter = new TestPresenter();
        CalculateTDEEInteractor interactor = new CalculateTDEEInteractor(bmrFormula, presenter);

        CalculateTDEEInputData input = new CalculateTDEEInputData(
                498,
                7.0,
                17.0,
                true,
                UserMetrics.Sex.MALE,
                ActivityLevel.MEDIUM,
                CalDevianceRate.LOSE_250wk
        );

        interactor.execute(input);

        assertNull(presenter.error);
        assertNotNull(presenter.output);
        assertEquals(0, presenter.output.bmr());
        assertEquals(0 * ActivityLevel.MEDIUM.multiplier + CalDevianceRate.MAINTAIN_0wk.devianceRate,
                presenter.output.tdee());

        assertEquals("Mifflin-St Jeor", presenter.output.formulaName());
    }

    @Test
    public void passImperialEqualMetric() {
        BMRFormula bmrFormula = new MifflinStJeorBMR();

        class TestPresenter implements CalculateTDEEOutputBoundary {
            CalculateTDEEOutputData output;
            String error;

            @Override
            public void present(CalculateTDEEOutputData data) { output = data; }

            @Override
            public void presentValidationError(String message) { error = message; }
        }

        TestPresenter pMetric = new TestPresenter();
        TestPresenter pImperial = new TestPresenter();

        CalculateTDEEInteractor interMetric = new CalculateTDEEInteractor(bmrFormula, pMetric);
        CalculateTDEEInteractor interImperial = new CalculateTDEEInteractor(bmrFormula, pImperial);

        CalculateTDEEInputData metricInput = new CalculateTDEEInputData(
                        25,
                        80.0,
                        180.0,
                true,
                UserMetrics.Sex.MALE,
                ActivityLevel.MEDIUM,
                CalDevianceRate.MAINTAIN_0wk
                );

        CalculateTDEEInputData imperialInput = new CalculateTDEEInputData(
                        25,
                        80.0 / 0.453592,
                        180.0 / 2.54,
                false,
                UserMetrics.Sex.MALE,
                ActivityLevel.MEDIUM,
                CalDevianceRate.MAINTAIN_0wk
                );

        interMetric.execute(metricInput);
        interImperial.execute(imperialInput);

        assertNull(pMetric.error);
        assertNull(pImperial.error);

        assertNotNull(pMetric.output);
        assertNotNull(pImperial.output);

        assertEquals(pMetric.output.bmr(), pImperial.output.bmr());
        assertEquals(pMetric.output.tdee(), pImperial.output.tdee());
    }

}
