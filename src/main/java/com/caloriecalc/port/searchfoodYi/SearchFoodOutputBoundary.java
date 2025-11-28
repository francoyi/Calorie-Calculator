package com.caloriecalc.port.searchfoodYi;

public interface SearchFoodOutputBoundary {

    /**
     * called when nutritional information is found
     */
    void presentSearchResult(ResponseData responseModel);

    /**
     * called when there is no matching result
     */
    void presentNoResult(String query);

    /**
     * called when an API call or JSON parsing error occurs
     */
    void presentError(String message);
}