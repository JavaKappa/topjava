package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DataJpaMealRepository implements MealRepository {

    @Autowired
    private CrudMealRepository crudRepository;
    @Autowired
    private CrudUserRepository crudUserRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Meal save(Meal meal, int userId) {
//        Meal m = crudRepository.getByIdAndUserId()
        User user = crudUserRepository.getOne(userId);
        meal.setUser(user);
        if (meal.isNew()) {
            return crudRepository.save(meal);
        } else {
            if (crudRepository.getByIdAndUserId(meal.getId(), userId)!= null) {
                return crudRepository.save(meal);
            }
        }
        return null;
    }


    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return crudRepository.deleteMealByIdAndUserId(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.getByIdAndUserId(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findMealsByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.findMealsByDateTimeBetweenAndUserIdOrderByDateTimeDesc(startDateTime, endDateTime.minusSeconds(1), userId);
    }
}
