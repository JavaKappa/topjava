package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 12, 0), "Завтрак тост", -101),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        if (meals.isEmpty()) {
            System.out.println("return empty list");
            return Collections.emptyList();
        }
        Objects.requireNonNull(startTime);
        Objects.requireNonNull(endTime);
        //Распределяем каллории по дням
        Map<LocalDate, Integer> dayAndCalories = new HashMap<>();
        meals.forEach(m -> {
            LocalDate day = m.getDateTime().toLocalDate();
            if (dayAndCalories.containsKey(day)) {
                int calories = dayAndCalories.get(day) + m.getCalories();
                dayAndCalories.put(day, calories);
            } else {
                dayAndCalories.put(day, m.getCalories());
            }
        });
        //заполняем всеми блюдами со значением excess
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        meals.forEach(m -> {
            if (dayAndCalories.get(m.getDateTime().toLocalDate()) > caloriesPerDay) {
                userMealWithExcesses.add(new UserMealWithExcess(m, true));
            } else {
                userMealWithExcesses.add(new UserMealWithExcess(m, false));
            }
        });

        //фильтруем
        userMealWithExcesses.removeIf(userMealWithExcess -> !TimeUtil.isBetweenInclusive(userMealWithExcess.getDateTime().toLocalTime(), startTime, endTime));

        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        System.out.println("TODO Implement by streams");
        //get UserMeals per day
        Map<LocalDate, Integer> dayAndCalories = meals.stream().collect(HashMap::new,
                (map, meal) -> {
                    LocalDate day = meal.getDateTime().toLocalDate();
                    if (map.containsKey(day)) {
                        int calories = map.get(day) + meal.getCalories();
                        map.put(day, calories);
                    } else {
                        map.put(day, meal.getCalories());
                    }
                },
                null);
        return null;
    }

}
