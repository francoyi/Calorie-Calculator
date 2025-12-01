package com.caloriecalc.usecase.myfoods.addmyfoodtomeal;

public class AddMyFoodToMealInteractor implements AddMyFoodToMealInputBoundary {

    private final AddMyFoodToMealOutputBoundary presenter;

    public AddMyFoodToMealInteractor(AddMyFoodToMealOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(AddMyFoodToMealInputData data) {
        AddMyFoodToMealOutputData out =
                new AddMyFoodToMealOutputData(
                        data.getFoodName(),
                        data.getAmount(),
                        data.getUnit(),
                        data.getTotalKcal()
                );

        presenter.present(out);
    }
}