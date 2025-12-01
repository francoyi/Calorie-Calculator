package Test.usecase.MifflinBMR;

import com.caloriecalc.model.ActivityLevel;
import com.caloriecalc.model.CalDevianceRate;
import com.caloriecalc.model.UserMetrics;
import com.caloriecalc.port.tdee.BmrFormula;
import com.caloriecalc.service.MifflinStJeorBmr;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class mifflinBmrFormulaTest {

    @Test
    void passCorrectName() {
        BmrFormula bmrFormula = new MifflinStJeorBmr();
        String bmrName = bmrFormula.name();

        assertEquals("Mifflin-St Jeor", bmrName);
    }

    @Test
    void passCalcBMR() {
        BmrFormula bmrFormula = new MifflinStJeorBmr();
        String bmrName = bmrFormula.name();

        UserMetrics userMetrics = new UserMetrics(
                19,
                70,
                180,
                UserMetrics.Sex.FEMALE,
                ActivityLevel.MEDIUM,
                CalDevianceRate.MAINTAIN_0wk,
                true
        );

        double userBMR = bmrFormula.computeBmr(userMetrics);

        assertEquals(1569.0, userBMR, 0.01);
    }

    @Test
    void passCalcNegBMR() {
        BmrFormula bmrFormula = new MifflinStJeorBmr();
        String bmrName = bmrFormula.name();

        UserMetrics userMetrics = new UserMetrics(
                190000,
                70,
                180,
                UserMetrics.Sex.FEMALE,
                ActivityLevel.MEDIUM,
                CalDevianceRate.MAINTAIN_0wk,
                true
        );

        double userBMR = bmrFormula.computeBmr(userMetrics);

        assertEquals(-948336.0, userBMR, 0.01);
    }

}


