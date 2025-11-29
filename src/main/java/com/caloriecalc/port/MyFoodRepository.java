package com.caloriecalc.port;

import com.caloriecalc.model.MyFood;

import java.util.List;
import java.util.Optional;

/**
 * Gateway interface for accessing locally saved user-created foods.
 *
 * This follows Clean Architecture:
 *  - The Interactor depends on this interface (not an implementation).
 *  - UI and controllers never touch implementations directly.
 *  - Data access implementations (JSON, in-memory, etc.) live in com.caloriecalc.repo.
 */
public interface MyFoodRepository {

    /**
     * Save or overwrite a food in the MyFoods catalog.
     */
    void save(MyFood food);

    /**
     * Check if a food with the given name already exists.
     */
    boolean existsByName(String name);

    /**
     * Find a food by name.
     */
    Optional<MyFood> findByName(String name);

    /**
     * Retrieve all saved foods.
     */
    List<MyFood> findAll();

    List<MyFood> getAllFoods();
}