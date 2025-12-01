package Test.usecase.TDEE;

import com.caloriecalc.entity.UserMetrics;
import com.caloriecalc.usecase.tdee.BmrFormula;

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
