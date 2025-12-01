package Test.usecase.TDEE;

import com.caloriecalc.model.UserMetrics;
import com.caloriecalc.port.tdee.BmrFormula;

public class TestBmr1600 implements BmrFormula {
    @Override
    public String name() {
        return "Testing BMR - 1600";
    }

    @Override
    public double computeBmr(UserMetrics userMetrics) {
        return 1600;
    }
}
