package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql",config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(USER_MEAL_1_ID, USER_ID);
        Meal meal2 = service.get(USER_MEAL_2_ID, USER_ID);
        assertMatch(USER_MEAL_1, meal);
        assertMatch(USER_MEAL_2, meal2);
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        service.delete(USER_MEAL_1_ID, USER_ID);
        Meal meal = service.get(USER_MEAL_1_ID, USER_ID);
        System.out.println(meal);
    }

    @Test
    public void getBetweenHalfOpen() {
        List<Meal> meals = service.getBetweenHalfOpen(LocalDate.now(), LocalDate.now().plusDays(1), 100001);
        assertMatch(meals, Arrays.asList(ADMIN_MEAL_1, ADMIN_MEAL_2));
    }

    @Test
    public void getAll() {
        List<Meal> actual = service.getAll(USER_ID);
        List<Meal> expected = Arrays.asList(USER_MEAL_1, USER_MEAL_2);
        assertMatch(actual, expected);
    }


    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        Meal getUpdateFromDB = service.get(USER_MEAL_1_ID, USER_ID);
        assertMatch(updated, getUpdateFromDB);
    }

    @Test
    public void create() {
        Meal newMeal = getNew();
        service.create(newMeal, USER_ID);
        Meal getFromDb = service.get(newMeal.getId() , USER_ID);
        assertMatch(newMeal,getFromDb);
    }
    @Test(expected = NotFoundException.class)
    public void getForeignFood() {
        service.get(ADMIN_MEAL_1_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateForeignFood() {
        Meal meal = getUpdated();
        service.update(meal, ADMIN_ID);
    }
    @Test(expected = NotFoundException.class)
    public void deleteForeignFood() {
        service.delete(100002, ADMIN_ID);
    }
}