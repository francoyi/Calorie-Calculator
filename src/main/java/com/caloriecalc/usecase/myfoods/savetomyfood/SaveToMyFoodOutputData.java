package com.caloriecalc.usecase.myfoods.savetomyfood;

import com.caloriecalc.entity.MyFood;

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