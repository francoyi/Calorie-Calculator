package Test.usecase.MifflinBMR;

import com.caloriecalc.model.ActivityLevel;
import com.caloriecalc.model.CalDevianceRate;
import com.caloriecalc.model.UserMetrics;
import com.caloriecalc.port.tdee.BMRFormula;
import com.caloriecalc.service.MifflinStJeorBMR;
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

}


