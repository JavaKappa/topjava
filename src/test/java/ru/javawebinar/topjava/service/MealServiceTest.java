package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import java.time.LocalDate;
import java.time.Month;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
//@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
@ActiveProfiles({Profiles.POSTGRES_DB, Profiles.DATAJPA})
public class MealServiceTest extends ServiceTest{
    private static final Logger log = LoggerFactory.getLogger(MealServiceTest.class);
    @Autowired
    private MealService service;
    @Autowired
    private MealRepository repository;

    @Test
    public void delete() throws Exception {
        log.debug("Starting delete test");
        service.delete(MEAL1_ID, USER_ID);
        Assert.assertNull(repository.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() throws Exception {
        log.debug("Starting deleteNotFound test");
        Assert.assertThrows(NotFoundException.class,
                () -> service.delete(1, USER_ID));
    }

    @Test
    public void deleteNotOwn() throws Exception {
        log.debug("Starting deleteNotOwn test");
        Assert.assertThrows(NotFoundException.class,
                () -> service.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void create() throws Exception {
        log.debug("Starting create test");
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, USER_ID);
        log.debug("end of save() transaction");
        Integer newId = created.getId();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(service.get(newId, USER_ID), newMeal);
    }
    @Test
    public void get() throws Exception {
        log.debug("Starting get test");
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() throws Exception {
        log.debug("Starting getNotFound test");
        Assert.assertThrows(NotFoundException.class,
                () -> service.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void getNotOwn() throws Exception {
        log.debug("Starting deleteNotOwn test");
        Assert.assertThrows(NotFoundException.class,
                () -> service.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void update() throws Exception {
        log.debug("Starting update test");
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        MEAL_MATCHER.assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() throws Exception {
        log.debug("Starting updateNorFound test");
        NotFoundException ex = Assert.assertThrows(NotFoundException.class,
                () -> service.update(MEAL1, ADMIN_ID));
        Assert.assertEquals("Not found entity with id=" + MEAL1_ID, ex.getMessage());
    }

    @Test
    public void getAll() throws Exception {
        log.debug("Starting getAll test");
        MEAL_MATCHER.assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetweenInclusive() throws Exception {
        log.debug("Starting getBetweenInclusive test");
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void getBetweenWithNullDates() throws Exception {
        log.debug("Starting getBetweenInclusive with null dates test");
        MEAL_MATCHER.assertMatch(service.getBetweenInclusive(null, null, USER_ID), MEALS);
    }
}