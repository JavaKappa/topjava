package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public interface Storage {
    void save(Meal meal);

    void delete(int id);

    void update(Meal meal);

    Meal load(int id);

    boolean isContainMeal(int id);

    List<Meal> getAllMeals();
}
