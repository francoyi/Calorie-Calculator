package com.caloriecalc.ui.myfoods;

import com.caloriecalc.model.MyFood;

/**
 * ViewModel for the MyFoods feature.
 *
 * Holds the user-visible state about:
 *  - Last status message from SaveToMyFood use case
 *  - The MyFood object that was just saved
 *
 * This ViewModel is read by the UI (dialogs) and written by the Presenter.
 */
public class MyFoodsViewModel {

    private String lastMessage;
    private MyFood lastSavedFood;

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public MyFood getLastSavedFood() {
        return lastSavedFood;
    }

    public void setLastSavedFood(MyFood lastSavedFood) {
        this.lastSavedFood = lastSavedFood;
    }
}
