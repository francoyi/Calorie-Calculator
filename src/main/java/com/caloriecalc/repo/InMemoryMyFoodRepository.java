package com.caloriecalc.repo;

import com.caloriecalc.model.MyFood;
import com.caloriecalc.port.MyFoodRepository;

import java.util.*;

/**
 * A temporary in-memory repository for storing user-created foods.
 */
public class InMemoryMyFoodRepository implements MyFoodRepository {

    private final Map<String, MyFood> store = new HashMap<>();

    public InMemoryMyFoodRepository() {
        // Optional: preload fake data for debugging
        // MyFood example = new MyFood("TestFood", List.of(), 123);
        // store.put(example.getName().toLowerCase(), example);
    }

    @Override
    public void save(MyFood food) {
        store.put(food.getName().toLowerCase(), food);
    }

    @Override
    public boolean existsByName(String name) {
        return store.containsKey(name.toLowerCase());
    }

    @Override
    public Optional<MyFood> findByName(String name) {
        return Optional.ofNullable(store.get(name.toLowerCase()));
    }

    @Override
    public List<MyFood> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<MyFood> getAllFoods() {
        return new ArrayList<>(store.values());
    }
}