package ru.javawebinar.topjava.model;

import javax.persistence.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId"),
        @NamedQuery(name = Meal.ALL_SORTED, query = "SELECT m FROM Meal m WHERE m.user.id=:userId ORDER BY m.dateTime DESC"),
        @NamedQuery(name = Meal.BETWEEN_HALF_OPEN, query = "SELECT m FROM Meal m WHERE m.user.id=:user_id  AND m.dateTime >=:date_time_start AND m.dateTime < :date_time_end  ORDER BY m.dateTime DESC"),
})
@Entity
@Table(name = "meals", uniqueConstraints = {@UniqueConstraint(columnNames = "date_time", name = "meals_unique_user_datetime_idx")})
public class Meal extends AbstractBaseEntity {
    public static final String DELETE = "Meal.delete";
    public static final String ALL_SORTED = "Meal.getAllSorted";
    public static final String BETWEEN_HALF_OPEN = "Meal.getBetweenHalfOpen";


    @Column(name = "date_time", unique = true, nullable = false)
    @NotNull
    private LocalDateTime dateTime;
    @Column(name = "description")
    @NotBlank
    private String description;
    @NotNull
    @Column(name = "calories")
    private int calories;


    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
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

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}


