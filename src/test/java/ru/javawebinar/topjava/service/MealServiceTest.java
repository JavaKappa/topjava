package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.TestMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    private static final Logger log = LoggerFactory.getLogger(MealServiceTest.class);
    private static Map<String, Long> testsMap = new HashMap<>();
    @Rule
    public TestName testName = new TestName();
    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Rule
    public TestRule rule = (statement, description) -> new Statement() {
        @Override
        public void evaluate() throws Throwable {
            Date start = new Date();
            statement.evaluate();
            Date finish = new Date();
            String methodName = testName.getMethodName();
            long timeWork = (finish.getTime() - start.getTime());
            testsMap.put(methodName, timeWork);
            log.debug("\n-----------------------------------------------------------------");
            log.debug("method " + methodName + " time work " + timeWork + " ms");
        }
    };
    @ClassRule
    public static TestRule classRule = (statement, description) -> new Statement() {
        @Override
        public void evaluate() throws Throwable {
            statement.evaluate();
            testsMap.forEach((name, time) -> log.info(name + " - " + time + " ms"));
            log.debug("\n---------------------------------------------------------------");
        }
    };

    @Autowired
    private MealService service;
    @Autowired
    private MealRepository repository;
    private static TestMatcher<Meal> matcher = TestMatcher.of("user");

    @Test
    public void delete() throws Exception {
        service.delete(MEAL1_ID, USER_ID);
        Assert.assertNull(repository.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() throws Exception {
        Throwable thrown = catchThrowable(() -> service.delete(1, USER_ID));
        assertThat(thrown).isInstanceOf(NotFoundException.class);
        assertThat(thrown.getMessage()).startsWith("Not found entity with");

    }

    @Test
    public void deleteNotOwn() throws Exception {
        Throwable thrown = catchThrowable(() -> service.delete(MEAL1_ID, ADMIN_ID));
        assertThat(thrown).isInstanceOf(NotFoundException.class);
        assertThat(thrown.getMessage()).startsWith("Not found entity with");
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = getCreated();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        matcher.assertMatch(created, newMeal);
        matcher.assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void get() throws Exception {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        matcher.assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() throws Exception {
        Throwable thrown = catchThrowable(() -> service.get(1, USER_ID));
        assertThat(thrown).isInstanceOf(NotFoundException.class);
        assertThat(thrown.getMessage()).startsWith("Not found entity with");
    }

    @Test
    public void getNotOwn() throws Exception {
        Throwable thrown = catchThrowable(() ->
                service.get(MEAL1_ID, ADMIN_ID)
                );
        assertThat(thrown).isInstanceOf(NotFoundException.class);
        assertThat(thrown.getMessage()).startsWith("Not found entity with");
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        matcher.assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() throws Exception {
        Throwable thrown = catchThrowable(() -> service.update(MEAL1, ADMIN_ID));
        assertThat(thrown).isInstanceOf(NotFoundException.class);
        assertThat(thrown.getMessage()).contains("Meal NotFound");

    }

    @Test
    public void getAll() throws Exception {
        matcher.assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetweenInclusive() throws Exception {
        matcher.assertMatch(service.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void getBetweenWithNullDates() throws Exception {
        matcher.assertMatch(service.getBetweenInclusive(null, null, USER_ID), MEALS);
    }
}