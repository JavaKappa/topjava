package ru.javawebinar.topjava;

import org.assertj.core.api.Assertions;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_1_ID = START_SEQ + 2;
    public static final int USER_MEAL_2_ID = START_SEQ + 3;
    public static final int ADMIN_MEAL_1_ID = START_SEQ + 4;
    public static final int ADMIN_MEAL_2_ID = START_SEQ + 5;

    public static final Meal USER_MEAL_1 = new Meal(USER_MEAL_1_ID, LocalDateTime.now(), "user_Meal 1", 500);
    public static final Meal USER_MEAL_2 = new Meal(USER_MEAL_2_ID, LocalDateTime.now(), "user_Meal 2", 750);
    public static final Meal ADMIN_MEAL_1 = new Meal(ADMIN_MEAL_1_ID, LocalDateTime.now(), "admin_Meal 1", 750);
    public static final Meal ADMIN_MEAL_2 = new Meal(ADMIN_MEAL_2_ID, LocalDateTime.now(), "admin_Meal 2", 123);

    public static void assertMatch(Meal expected, Meal actual) {
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> expected, Iterable<Meal> actual) {
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    public static Meal getUpdated() {
        Meal meal = new Meal(USER_MEAL_1);
        meal.setDescription("Updated_name");
        meal.setCalories(-222);
        meal.setDateTime(LocalDateTime.of(1726, 4, 3, 2, 2));
        return meal;
    }

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2020, 12, 31, 23, 59), "New Food", 1600);
    }

}
