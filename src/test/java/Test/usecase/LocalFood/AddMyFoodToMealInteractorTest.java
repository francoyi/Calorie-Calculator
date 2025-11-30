package Test.usecase.LocalFood;

import com.caloriecalc.port.addmyfoodtomeal.*;
import com.caloriecalc.service.AddMyFoodToMealInteractor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddMyFoodToMealInteractorTest {

    // Fake presenter that captures the output
    static class FakePresenter implements AddMyFoodToMealOutputBoundary {
        AddMyFoodToMealOutputData last;

        @Override
        public void present(AddMyFoodToMealOutputData data) {
            last = data;
        }
    }

    @Test
    void testAddMyFoodToMeal() {
        // Arrange
        FakePresenter presenter = new FakePresenter();
        AddMyFoodToMealInteractor interactor =
                new AddMyFoodToMealInteractor(presenter);

        AddMyFoodToMealInputData input =
                new AddMyFoodToMealInputData(
                        "Pasta",
                        550.0   // total kcal
                );

        // Act
        interactor.execute(input);

        // Assert: presenter got the correct output
        assertNotNull(presenter.last);
        assertEquals("Pasta", presenter.last.getFoodName());
        assertEquals(100.0, presenter.last.getAmount());
        assertEquals("g", presenter.last.getUnit());
        assertEquals(550.0, presenter.last.getTotalKcal());
    }

    @Test
    void testDifferentFoodValues() {
        // Arrange
        FakePresenter presenter = new FakePresenter();
        AddMyFoodToMealInteractor interactor =
                new AddMyFoodToMealInteractor(presenter);

        AddMyFoodToMealInputData input =
                new AddMyFoodToMealInputData(
                        "Chicken",   // name
                        300.0        // total kcal
                );

        // Act
        interactor.execute(input);

        // Assert
        assertNotNull(presenter.last);
        assertEquals("Chicken", presenter.last.getFoodName());
        assertEquals(100.0, presenter.last.getAmount());
        assertEquals("g", presenter.last.getUnit());
        assertEquals(300.0, presenter.last.getTotalKcal());
    }
}
