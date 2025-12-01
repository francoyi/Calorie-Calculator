package Test.usecase.TDEE;

import com.caloriecalc.model.ActivityLevel;
import com.caloriecalc.model.CalDevianceRate;
import com.caloriecalc.model.UserMetrics;
import com.caloriecalc.port.tdee.BmrFormula;
import com.caloriecalc.port.tdee.CalculateTdeeInputData;
import com.caloriecalc.port.tdee.CalculateTdeeOutputBoundary;
import com.caloriecalc.port.tdee.CalculateTdeeOutputData;
import com.caloriecalc.service.CalculateTdeeInteractor;
import com.caloriecalc.service.MifflinStJeorBmr;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TDEEInteractorTest {
    @Test
    public void failureAgeNotAbove18() {
        BmrFormula bmrFormula = new BmrFormula() {
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

        class TestPresenter implements CalculateTdeeOutputBoundary {
            String validationErrorMessage = null;
            CalculateTdeeOutputData outputData = null;

            @Override
            public void present(CalculateTdeeOutputData data) {
                this.outputData = data;
            }

            @Override
            public void presentValidationError(String message) {
                this.validationErrorMessage = message;
            }
        }

        TestPresenter presenter = new TestPresenter();
        CalculateTdeeInteractor interactor = new CalculateTdeeInteractor(bmrFormula, presenter);

        CalculateTdeeInputData input = new CalculateTdeeInputData(
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
        BmrFormula bmrFormula = new BmrFormula() {
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

        class TestPresenter implements CalculateTdeeOutputBoundary {
            String validationErrorMessage = null;
            CalculateTdeeOutputData outputData = null;

            @Override
            public void present(CalculateTdeeOutputData data) {
                this.outputData = data;
            }

            @Override
            public void presentValidationError(String message) {
                this.validationErrorMessage = message;
            }
        }

        TestPresenter presenter = new TestPresenter();
        CalculateTdeeInteractor interactor = new CalculateTdeeInteractor(bmrFormula, presenter);

        CalculateTdeeInputData input = new CalculateTdeeInputData(
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
        BmrFormula bmrFormula = new BmrFormula() {
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

        class TestPresenter implements CalculateTdeeOutputBoundary {
            String validationErrorMessage = null;
            CalculateTdeeOutputData outputData = null;

            @Override
            public void present(CalculateTdeeOutputData data) {
                this.outputData = data;
            }

            @Override
            public void presentValidationError(String message) {
                this.validationErrorMessage = message;
            }
        }

        TestPresenter presenter = new TestPresenter();
        CalculateTdeeInteractor interactor = new CalculateTdeeInteractor(bmrFormula, presenter);

        CalculateTdeeInputData input = new CalculateTdeeInputData(
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
        BmrFormula bmrFormula = new TestBmr1600();

        class TestPresenter implements CalculateTdeeOutputBoundary {
            CalculateTdeeOutputData output;
            String error;

            @Override
            public void present(CalculateTdeeOutputData data) {
                this.output = data;
            }

            @Override
            public void presentValidationError(String message) {
                this.error = message;
            }
        }

        TestPresenter presenter = new TestPresenter();
        CalculateTdeeInteractor interactor = new CalculateTdeeInteractor(bmrFormula, presenter);

        CalculateTdeeInputData input = new CalculateTdeeInputData(
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
        BmrFormula bmrFormula = new MifflinStJeorBmr();
        class TestPresenter implements CalculateTdeeOutputBoundary {
            CalculateTdeeOutputData output;
            String error;

            @Override
            public void present(CalculateTdeeOutputData data) {
                this.output = data;
            }

            @Override
            public void presentValidationError(String message) {
                this.error = message;
            }
        }

        TestPresenter presenter = new TestPresenter();
        CalculateTdeeInteractor interactor = new CalculateTdeeInteractor(bmrFormula, presenter);

        CalculateTdeeInputData input = new CalculateTdeeInputData(
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
        BmrFormula bmrFormula = new MifflinStJeorBmr();
        class TestPresenter implements CalculateTdeeOutputBoundary {
            CalculateTdeeOutputData output;
            String error;

            @Override
            public void present(CalculateTdeeOutputData data) {
                this.output = data;
            }

            @Override
            public void presentValidationError(String message) {
                this.error = message;
            }
        }

        TestPresenter presenter = new TestPresenter();
        CalculateTdeeInteractor interactor = new CalculateTdeeInteractor(bmrFormula, presenter);

        CalculateTdeeInputData input = new CalculateTdeeInputData(
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
        BmrFormula bmrFormula = new MifflinStJeorBmr();

        class TestPresenter implements CalculateTdeeOutputBoundary {
            CalculateTdeeOutputData output;
            String error;

            @Override
            public void present(CalculateTdeeOutputData data) { output = data; }

            @Override
            public void presentValidationError(String message) { error = message; }
        }

        TestPresenter pMetric = new TestPresenter();
        TestPresenter pImperial = new TestPresenter();

        CalculateTdeeInteractor interMetric = new CalculateTdeeInteractor(bmrFormula, pMetric);
        CalculateTdeeInteractor interImperial = new CalculateTdeeInteractor(bmrFormula, pImperial);

        CalculateTdeeInputData metricInput = new CalculateTdeeInputData(
                        25,
                        80.0,
                        180.0,
                true,
                UserMetrics.Sex.MALE,
                ActivityLevel.MEDIUM,
                CalDevianceRate.MAINTAIN_0wk
                );

        CalculateTdeeInputData imperialInput = new CalculateTdeeInputData(
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
