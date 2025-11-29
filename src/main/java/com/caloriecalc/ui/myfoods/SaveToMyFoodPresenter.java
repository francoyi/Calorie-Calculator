package com.caloriecalc.ui.myfoods;

import com.caloriecalc.model.MyFood;
import com.caloriecalc.port.savetomyfood.SaveToMyFoodOutputBoundary;
import com.caloriecalc.port.savetomyfood.SaveToMyFoodOutputData;

/**
 * Presenter for the SaveToMyFood use case.
 *
 * This class adapts Output Data from the Interactor
 * into a format that the View (Swing dialogs) can use.
 */
public class SaveToMyFoodPresenter implements SaveToMyFoodOutputBoundary {

    private final MyFoodsViewModel viewModel;

    public SaveToMyFoodPresenter(MyFoodsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(SaveToMyFoodOutputData data) {

        viewModel.setLastMessage(data.getMessage());
        viewModel.setLastSavedFood(data.getSavedFood());

    }
}