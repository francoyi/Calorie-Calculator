package Test.usecase.MifflinBMR;

import com.caloriecalc.entity.ActivityLevel;
import com.caloriecalc.entity.CalDevianceRate;
import com.caloriecalc.entity.UserMetrics;
import com.caloriecalc.usecase.tdee.BMRFormula;
import com.caloriecalc.entity.MifflinStJeorBMR;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class mifflinBmrFormulaTest {

    @Test
    void passCorrectName() {
        BMRFormula bmrFormula = new MifflinStJeorBMR();
        String bmrName = bmrFormula.name();

        assertEquals("Mifflin-St Jeor", bmrName);
    }

    @Test
    void passCalcBMR() {
        BMRFormula bmrFormula = new MifflinStJeorBMR();
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
        BMRFormula bmrFormula = new MifflinStJeorBMR();
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


