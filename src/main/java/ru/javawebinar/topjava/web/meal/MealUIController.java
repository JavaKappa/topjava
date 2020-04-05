package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/ajax/profile/meals")
public class MealUIController extends AbstractMealController{

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void createOrUpdate(@RequestParam Integer id,
                               @RequestParam LocalDateTime dateTime,
                               @RequestParam String description,
                               @RequestParam Integer calories) {
        Meal meal = new Meal(id, dateTime, description, calories);
        if (meal.isNew()) {
            super.create(meal);
        }
    }


}