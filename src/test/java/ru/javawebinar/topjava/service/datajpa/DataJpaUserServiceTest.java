package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.JpaUtil;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {
    @Autowired
    protected JpaUtil jpaUtil;


    @Override
    public void setUp() throws Exception {
        super.setUp();
        jpaUtil.clear2ndLevelHibernateCache();
    }

    @Test
    public void getWithMeals() throws Exception {
        User user = service.getWithMeals(USER_ID);
        USER_MATCHER.assertMatch(user, USER);
        MealTestData.MEAL_MATCHER.assertMatch(user.getMeals(), MealTestData.MEALS);
    }

    @Test
    public void getWithMealsNotFound() throws Exception {
        Assert.assertThrows(NotFoundException.class,
                () -> service.getWithMeals(1));
    }
}