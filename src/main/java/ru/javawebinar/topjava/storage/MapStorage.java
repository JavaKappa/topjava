package ru.javawebinar.topjava.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MapStorage implements Storage {
    private static final Logger log = LoggerFactory.getLogger(MapStorage.class);
    private ConcurrentHashMap<Integer, Meal> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Meal meal) {
        log.debug("Save meal with id" + meal.getId());
        if (storage.containsKey(meal.getId())) {
            log.error("meal already exist");
            return;
        }
        storage.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        log.debug("Deleting meal with id" + id);
        if (storage.remove(id) == null)
            log.error("meal alreafy exist");
    }

    @Override
    public void update(Meal meal) {
        log.debug("update meal with id " + meal.getId());
        if (storage.containsKey(meal.getId())) {
            storage.put(meal.getId(), meal);
        } else {
            log.error("Meal with id " + meal.getId() + " does not exist");
        }
    }

    @Override
    public Meal load(int id) {
        log.debug("Loading meal with id " + id);
        Meal meal = storage.get(id);
        if (meal == null) log.error("meal does not exist");
        return meal;
    }

    @Override
    public boolean isContainMeal(int id) {
        return storage.containsKey(id);
    }

    @Override
    public List<Meal> getAllMeals() {
        log.debug("Getting all meals");
        return new ArrayList<>(storage.values());
    }
}
