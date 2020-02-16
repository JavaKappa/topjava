package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.ValidationUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);
    private MealService service;



    public List<Meal> getAll() {
        int userId = SecurityUtil.authUserId();
        return service.getAll(userId);
    }

    public Meal get(int id) {
        return service.get(SecurityUtil.authUserId(), id);
    }

    public void delete(int id) {
        service.delete(SecurityUtil.authUserId(), id);
    }

    public Meal create(Meal meal) {
        checkNew(meal);
        return service.create(SecurityUtil.authUserId(), meal);
    }

    public void update(Meal meal, int id) {
        assureIdConsistent(meal, id);
        service.update(SecurityUtil.authUserId(), meal);
    }


}