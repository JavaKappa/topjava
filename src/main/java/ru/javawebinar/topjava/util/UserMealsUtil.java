package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 12, 0), "Завтрак тост", 101),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
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
        //Filter List<UserMeal> on valid time
        //Don't forget ask about if we take argument in method can we change him or not! How is right in production?
        List<UserMeal> mealsClone = new ArrayList<>(meals);
        mealsClone.removeIf(userMealWithExcess -> !TimeUtil.isBetweenInclusive(userMealWithExcess.getDateTime().toLocalTime(), startTime, endTime));
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        mealsClone.forEach(m -> userMealWithExcesses.add(dayAndCalories.get(m.getDateTime().toLocalDate()) > caloriesPerDay ? new UserMealWithExcess(m, true) : new UserMealWithExcess(m, false)));
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        System.out.println("TODO Implement by streams");
        //get UserMeals per day
        Map<LocalDate, Integer> dayAndCalories = meals.stream().collect(Collectors.toMap(x -> x.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));
        //now we filter on LocalTime value, then convert UserMeals list to UserMealsWithExcess
        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenInclusive(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> {
                    LocalDate date = meal.getDateTime().toLocalDate();
                    return new UserMealWithExcess(meal, dayAndCalories.get(date) > caloriesPerDay);
                }).collect(Collectors.toList());
    }
}
