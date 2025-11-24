package Test.usecase.TDEE;

import com.caloriecalc.model.UserMetrics;
import com.caloriecalc.port.tdee.BMRFormula;

public class TestBMR1600 implements BMRFormula {
    @Override
    public String name() {
        return "Testing BMR - 1600";
    }

    @Override
    public double computeBmr(UserMetrics a) {
        return 1600;
    }
}
