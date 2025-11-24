package com.caloriecalc.model;

public enum CalDevianceRate {
    MAINTAIN_0wk(0),
    LOSE_250wk(-275),
    LOSE_500wk(-550),
    GAIN_250wk(275),
    GAIN_500wk(550);


    public final int deviancerate;

    CalDevianceRate(int deviancerate) {
        this.deviancerate = deviancerate;
    }
}
