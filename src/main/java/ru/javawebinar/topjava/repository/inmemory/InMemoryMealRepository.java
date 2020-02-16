package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);


    @Override
    public Meal save(int userId, Meal meal) {
        log.debug(String.format("Save meal %s to user with id %d", meal.getDescription(), userId));
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (meal.isNew()) {
            meal.setId(counter.getAndIncrement());
            userMeals.put(meal.getId(), meal);
            return meal;
        }
        return userMeals.computeIfPresent(userId, (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        log.debug(String.format("delete meal with id %d from user with id %d", id, userId));
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals.remove(id) == null) {
            throw new NotFoundException(String.format("meal with id %s does not exist in user meals with id + %s", id, userId));
        }
        return true;
    }

    @Override
    public Meal get(int userId, int id) {
        log.debug(String.format("geting meal with id %d from User with id %d", id, userId));
        Map<Integer, Meal> userMeals = repository.get(userId);
        Meal meal = userMeals.get(id);
        if (meal == null) {
            log.error("meal with id %d at user with id %d does not exist");
            throw new NotFoundException(String.format("meal with id %d at user with id %d does not exist", id, userId));
        }
        return meal;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.get(userId).values()
                .stream()
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toList());
    }
}

