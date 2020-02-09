package ru.javawebinar.topjava.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Meal implements Serializable {
    private static final AtomicInteger ID_CREATOR = new AtomicInteger(0);

    private final int ID;
    private LocalDateTime dateTime;
    private String description;
    private int calories;

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this.ID = ID_CREATOR.getAndIncrement();
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public int getId() {
        return ID;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    @Override
    public String toString() {
        return "Meal{" +
                "ID=" + ID +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}
