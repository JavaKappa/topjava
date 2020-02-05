package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Function;
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
        Date start = new Date();
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        Date finish = new Date();
        mealsTo.forEach(System.out::println);
        System.out.println("processing " +  (finish.getTime() - start.getTime()) + " ms");

        Date startStream = new Date();
        List<UserMealWithExcess> mealsTo1 = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        Date finishStream = new Date();
        mealsTo1.forEach(System.out::println);
        System.out.println("processing " + (finishStream.getTime() - startStream.getTime()) + " ms");

        Date startOneStream = new Date();
        List<UserMealWithExcess> mealsTo2 = filteredByStreamsInOneStream(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        Date finishOneStream = new Date();
        mealsTo2.forEach(System.out::println);
        System.out.println("processing " + (finishOneStream.getTime() - startOneStream.getTime()) + " ms");

    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dayAndCalories = new HashMap<>();
        meals.forEach(m -> dayAndCalories.merge(m.getDateTime().toLocalDate(), m.getCalories(), Integer::sum));
        List<UserMealWithExcess> userMealsWithExcesses = new ArrayList<>();
        meals.forEach(meal -> {
            if (TimeUtil.isBetweenInclusive(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealsWithExcesses.add(convertToUserMealWithExcess(meal, dayAndCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        });
        return userMealsWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dayAndCalories = meals.stream().collect(Collectors.toMap(x -> x.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));
        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenInclusive(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> convertToUserMealWithExcess(meal, dayAndCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static List<UserMealWithExcess> filteredByStreamsInOneStream(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.groupingBy(m -> m.getDateTime().toLocalDate()))
                .values()
                .stream()
                .map(meals1 -> {
                    int cal = meals1.stream().mapToInt(UserMeal::getCalories).sum();
                    boolean exceed = cal > caloriesPerDay;
                    return meals1.stream()
                            .map(meal -> convertToUserMealWithExcess(meal, exceed))
                            .collect(Collectors.toList());
                })
                .flatMap(Collection::stream)
                .filter((meal -> TimeUtil.isBetweenInclusive(meal.getDateTime().toLocalTime(), startTime, endTime)))
                .collect(Collectors.toList());
    }

    public static UserMealWithExcess convertToUserMealWithExcess(UserMeal meal, boolean exceed) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceed);
    }
}
