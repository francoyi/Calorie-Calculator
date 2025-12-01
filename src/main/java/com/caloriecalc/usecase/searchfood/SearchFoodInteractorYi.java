package com.caloriecalc.usecase.searchfood;

import com.caloriecalc.entity.NutritionValues;

/**
 * Use Case: Search Food via External API (Yi's version).
 *
 * InputBoundary -> Interactor -> NutritionDataProvider -> OutputBoundary structure.
 */
public class SearchFoodInteractorYi implements SearchFoodInputBoundary {

    private final NutritionDataProvider provider;
    private final SearchFoodOutputBoundary presenter;

    public SearchFoodInteractorYi(NutritionDataProvider provider,
                                  SearchFoodOutputBoundary presenter) {
        this.provider = provider;
        this.presenter = presenter;
    }

    @Override
    public void searchFood(RequestData request) {
        String name = request.name();
        double amount = request.amount();
        String unit = request.unit();

        try {

            NutritionValues nv = provider.fetchNutritionPer100(name);

            if (nv == null || nv.kcalPer100g() == null) {
                // No Result
                presenter.presentNoResult(name);
                return;
            }

            // Calculate the kcal for this serving
            Double kcalPer100 = nv.kcalPer100g();
            Double kcalServing = null;

            if ("g".equalsIgnoreCase(unit) || "ml".equalsIgnoreCase(unit)) {
                kcalServing = kcalPer100 * (amount / 100.0);
            } else {

                kcalServing = kcalPer100;
            }

            ResponseData resp = new ResponseData(
                    name,
                    amount,
                    unit,
                    kcalServing
            );
            presenter.presentSearchResult(resp);

        } catch (RuntimeException e) {
            presenter.presentError("Failed to fetch nutrition for '" + name
                    + "': " + e.getMessage());
        }
    }
}
