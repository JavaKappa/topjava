package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    int deleteMealByIdAndUserId(int id, int userId);

    @Query("FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
    Meal getByIdAndUserId(@Param("id") int id,@Param("userId") int userId);


    @Query("FROM Meal m WHERE m.user.id=:userId AND m.dateTime >=:startDateTime AND m.dateTime <:endDateTime ORDER BY m.dateTime DESC ")
    List<Meal> getBetweenHalfOpenAndUserId(@Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime, @Param("userId") int userId);

    List<Meal> findMealsByUserIdOrderByDateTimeDesc(int userId);
}
