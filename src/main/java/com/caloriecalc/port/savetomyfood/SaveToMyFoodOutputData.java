package com.caloriecalc.port.savetomyfood;

import com.caloriecalc.model.MyFood;

/**
 * Output Data for the SaveToMyFood use case.
 *
 * The presenter will convert this into a ViewModel update.
 */
public class SaveToMyFoodOutputData {

    private final boolean success;
    private final String message;
    private final MyFood savedFood;

    public SaveToMyFoodOutputData(boolean success, String message, MyFood savedFood) {
        this.success = success;
        this.message = message;
        this.savedFood = savedFood;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public MyFood getSavedFood() {
        return savedFood;
    }
}
