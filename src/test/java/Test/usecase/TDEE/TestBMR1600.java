package Test.usecase.TDEE;

import com.caloriecalc.entity.UserMetrics;
import com.caloriecalc.usecase.tdee.BMRFormula;

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
