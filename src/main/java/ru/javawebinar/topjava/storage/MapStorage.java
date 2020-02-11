package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MapStorage implements Storage {
    private ConcurrentHashMap<Integer, Meal> storage = new ConcurrentHashMap<>();
    @Override
    public void save(Meal meal) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(Meal meal) {

    }

    @Override
    public Meal load(int id) {
        return null;
    }

    @Override
    public boolean isContainMeal(int id) {
        return false;
    }

    @Override
    public List<Meal> getAllMeals() {
        return null;
    }
}
