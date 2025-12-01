package Test.usecase.LocalFood;

import com.caloriecalc.entity.Ingredient;
import com.caloriecalc.entity.MyFood;
import com.caloriecalc.usecase.myfoods.MyFoodRepository;
import com.caloriecalc.usecase.myfoods.listmyfoods.ListMyFoodsInteractor;

import com.caloriecalc.usecase.myfoods.listmyfoods.ListMyFoodsInputData;
import com.caloriecalc.usecase.myfoods.listmyfoods.ListMyFoodsOutputBoundary;
import com.caloriecalc.usecase.myfoods.listmyfoods.ListMyFoodsOutputData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListMyFoodsInteractorTest {

    // Fake repo that returns a predefined list
    static class FakeRepo implements MyFoodRepository {
        List<MyFood> foods;

        FakeRepo(List<MyFood> foods) {
            this.foods = foods;
        }

        @Override
        public List<MyFood> getAllFoods() {
            return foods;
        }

        // Unused methods
        @Override public void save(MyFood food) {}
        @Override public boolean existsByName(String name) { return false; }
        @Override public java.util.Optional<MyFood> findByName(String name) { return java.util.Optional.empty(); }
        @Override public List<MyFood> findAll() { return List.of(); }
    }

    static class FakePresenter implements ListMyFoodsOutputBoundary {
        ListMyFoodsOutputData last;

        @Override
        public void present(ListMyFoodsOutputData data) {
            last = data;
        }
    }

    @Test
    void testListFoodsReturnsDataToPresenter() {
        // Arrange
        Ingredient blueberries = new Ingredient("Blueberries", 57.0, "g", 100.0);
        Ingredient spinach = new Ingredient("Spinach", 23.0, "g", 50.0);
        Ingredient pumpkin = new Ingredient("Pumpkin", 50.0, "g", 80.0);

        MyFood f1 = new MyFood("Smoothie", List.of(blueberries, spinach),150.0);
        MyFood f2 = new MyFood("Salad", List.of(spinach,pumpkin),130.0);

        FakeRepo repo = new FakeRepo(List.of(f1, f2));
        FakePresenter presenter = new FakePresenter();

        ListMyFoodsInteractor interactor =
                new ListMyFoodsInteractor(repo, presenter);

        // Act
        interactor.execute(new ListMyFoodsInputData());

        // Assert
        assertNotNull(presenter.last);
        assertEquals(2, presenter.last.getFoods().size());
        assertEquals("Smoothie", presenter.last.getFoods().get(0).getName());
        assertEquals("Salad", presenter.last.getFoods().get(1).getName());
    }

    @Test
    void testListFoodsEmptyList() {
        // Arrange
        FakeRepo repo = new FakeRepo(List.of());
        FakePresenter presenter = new FakePresenter();

        ListMyFoodsInteractor interactor =
                new ListMyFoodsInteractor(repo, presenter);

        // Act
        interactor.execute(new ListMyFoodsInputData());

        // Assert
        assertNotNull(presenter.last);
        assertTrue(presenter.last.getFoods().isEmpty());
    }
}
