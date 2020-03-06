package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    int deleteMealByIdAndUserId(int id, int userId);


    Meal getByIdAndUserId(int id, int userId);

    List<Meal> findMealsByDateTimeBetweenAndUserIdOrderByDateTimeDesc(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId);

    @Query("FROM User user WHERE user.id=:userId")
    User getUserById(@Param("userId") int userId);

    public List<Meal> findMealsByUserIdOrderByDateTimeDesc(int userId);
}
