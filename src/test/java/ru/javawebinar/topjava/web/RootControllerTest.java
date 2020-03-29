package ru.javawebinar.topjava.web;

import org.assertj.core.matcher.AssertionMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void getUsers() throws Exception {
        perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"))
                .andExpect(model().attribute("users",
                        new AssertionMatcher<List<User>>() {
                            @Override
                            public void assertion(List<User> actual) throws AssertionError {
                                USER_MATCHER.assertMatch(actual, ADMIN, USER);
                            }
                        }
                ));
    }
    @Test
    void getMeals() throws Exception{
        perform(MockMvcRequestBuilders.get("/meals"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.view().name("meals"))
        .andExpect(MockMvcResultMatchers.forwardedUrl("/WEB-INF/jsp/meals.jsp"))
        .andExpect(MockMvcResultMatchers.model().attribute("meals", new AssertionMatcher<List<MealTo>>() {
            @Override
            public void assertion(List<MealTo> mealTos) throws AssertionError {
                MEALTO_MATCHER.assertMatch(mealTos, MealsUtil.getTos(MEALS, SecurityUtil.authUserCaloriesPerDay()));
            }
        }));
    }
}