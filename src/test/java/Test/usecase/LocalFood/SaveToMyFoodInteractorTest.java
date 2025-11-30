package Test.usecase.LocalFood;
import com.caloriecalc.model.Ingredient;
import com.caloriecalc.model.MyFood;
import com.caloriecalc.port.MyFoodRepository;
import com.caloriecalc.port.savetomyfood.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import com.caloriecalc.service.SaveToMyFoodInteractor;

import static org.junit.jupiter.api.Assertions.*;

class SaveToMyFoodInteractorTest {

    /** A fake in-memory repository for testing. */
    static class FakeRepo implements MyFoodRepository {
        MyFood saved;

        @Override
        public void save(MyFood food) {
            this.saved = food;
        }

        @Override
        public boolean existsByName(String name) {
            return false;
        }

        @Override
        public Optional<MyFood> findByName(String name) {
            return Optional.empty();
        }

        @Override
        public java.util.List<MyFood> findAll() {
            return List.of();
        }

        @Override
        public java.util.List<MyFood> getAllFoods() {
            return List.of();
        }
    }

    /** A fake presenter for testing. */
    static class FakePresenter implements SaveToMyFoodOutputBoundary {
        SaveToMyFoodOutputData last;
        @Override
        public void present(SaveToMyFoodOutputData data) {
            last = data;
        }
    }

    @Test
    void testSuccessfulSave() {
        // Arrange
        FakeRepo repo = new FakeRepo();
        FakePresenter presenter = new FakePresenter();
        SaveToMyFoodInteractor interactor =
                new SaveToMyFoodInteractor(repo, presenter);

        Ingredient ing = new Ingredient("Banana", 89.0, "g",50.0);
        SaveToMyFoodInputData input =
                new SaveToMyFoodInputData("Smoothie", List.of(ing));

        // Act
        interactor.execute(input);

        // Assert: The presenter receives success
        assertNotNull(presenter.last);
        assertTrue(presenter.last.isSuccess());
        assertEquals("Food saved to My Foods.", presenter.last.getMessage());

        // Assert: The repository stored the food
        assertNotNull(repo.saved);
        assertEquals("Smoothie", repo.saved.getName());
        assertEquals(50.0, repo.saved.getTotalKcal());
    }


    @Test
    void testEmptyNameFails() {
        FakeRepo repo = new FakeRepo();
        FakePresenter presenter = new FakePresenter();
        SaveToMyFoodInteractor interactor =
                new SaveToMyFoodInteractor(repo, presenter);

        SaveToMyFoodInputData input =
                new SaveToMyFoodInputData("", List.of(new Ingredient("A", 10, "g",5.0)));

        interactor.execute(input);

        assertFalse(presenter.last.isSuccess());
        assertEquals("Food name cannot be empty.", presenter.last.getMessage());
    }

    @Test
    void testEmptyIngredientsFails() {
        FakeRepo repo = new FakeRepo();
        FakePresenter presenter = new FakePresenter();
        SaveToMyFoodInteractor interactor =
                new SaveToMyFoodInteractor(repo, presenter);

        SaveToMyFoodInputData input =
                new SaveToMyFoodInputData("TestFood", List.of());

        interactor.execute(input);

        assertFalse(presenter.last.isSuccess());
        assertEquals("At least one ingredient is required.", presenter.last.getMessage());
    }

    @Test
    void testNullIngredientListFails() {
        FakeRepo repo = new FakeRepo();
        FakePresenter presenter = new FakePresenter();
        SaveToMyFoodInteractor interactor =
                new SaveToMyFoodInteractor(repo, presenter);

        SaveToMyFoodInputData input =
                new SaveToMyFoodInputData("Smoothie", null);

        interactor.execute(input);

        assertFalse(presenter.last.isSuccess());
        assertEquals("At least one ingredient is required.", presenter.last.getMessage());
    }


}
