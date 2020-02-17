package ru.javawebinar.topjava.web;

import java.time.LocalDate;
import java.time.LocalTime;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {

    private static int caloriesPerDay = DEFAULT_CALORIES_PER_DAY;
    private static LocalDate startDate = LocalDate.MIN;
    private static LocalDate endDate = LocalDate.MAX;
    private static LocalTime startTime = LocalTime.MIN;
    private static LocalTime endTime = LocalTime.MAX;

    public static int getCaloriesPerDay() {
        return caloriesPerDay;
    }

    public static void setCaloriesPerDay(int caloriesPerDay) {
        SecurityUtil.caloriesPerDay = caloriesPerDay;
    }

    public static LocalDate getStartDate() {
        return startDate;
    }

    public static void setStartDate(LocalDate startDate) {
        SecurityUtil.startDate = startDate;
    }

    public static LocalDate getEndDate() {
        return endDate;
    }

    public static void setEndDate(LocalDate endDate) {
        SecurityUtil.endDate = endDate;
    }

    public static LocalTime getStartTime() {
        return startTime;
    }

    public static void setStartTime(LocalTime startTime) {
        SecurityUtil.startTime = startTime;
    }

    public static LocalTime getEndTime() {
        return endTime;
    }

    public static void setEndTime(LocalTime endTime) {
        SecurityUtil.endTime = endTime;
    }

    public static int authUserId() {
        return 1;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }


}