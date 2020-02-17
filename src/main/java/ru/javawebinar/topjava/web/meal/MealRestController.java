package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);
    private MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }


    public List<MealTo> getAll() {
        log.debug("getAll{}");
        int userId = SecurityUtil.authUserId();
        return MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        log.debug(String.format("get meal with id %s", id));
        return service.get(SecurityUtil.authUserId(), id);
    }

    public void delete(int id) {
        log.debug(String.format("delete  meal with id %s", id));
        service.delete(SecurityUtil.authUserId(), id);
    }


    public Meal create(Meal meal) {
        log.debug("create meal " + meal);
        checkNew(meal);
        return service.create(SecurityUtil.authUserId(), meal);
    }

    public Meal create() {
        return new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
    }

    public void update(Meal meal) {
        log.debug(String.format("update meal %s", meal));
        assureIdConsistent(meal, meal.getId());
        service.update(SecurityUtil.authUserId(), meal);
    }


}