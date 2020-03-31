package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Капу пк
 * 30.03.2020
 */
public class MealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MealRestController.REST_URL + "/";

    @Autowired
    private MealService service;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MealTestData.MEAL1_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(MealTestData.MEAL_MATCHER.contentJson(MealTestData.MEAL1));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MealTestData.MEAL1_ID))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThrows(NotFoundException.class, () -> service.delete(MealTestData.MEAL1_ID, UserTestData.USER_ID));
    }

    @Test
    void update() throws Exception {
        Meal updated = MealTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + MealTestData.MEAL1_ID)
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        MealTestData.MEAL_MATCHER.assertMatch(service.get(MealTestData.MEAL1_ID, UserTestData.USER_ID), updated);
    }

    @Test
    void create() throws Exception {
        Meal newMeal = MealTestData.getNew();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        Meal mealFromDb = service.get(100011, UserTestData.USER_ID);
        newMeal.setId(mealFromDb.getId());
        MealTestData.MEAL_MATCHER.assertMatch(mealFromDb, newMeal);
    }

    @Test
    void getBetween() throws Exception {
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("startDateTime", "2020-01-31T00:00:00.000")
                .param("endDateTime", "2020-01-31T23:59:00.000"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andReturn();
        String responseJson = result.getResponse().getContentAsString();
        String expectedResponseJson = JsonUtil.writeValue(MealsUtil.getTos(service.getBetweenInclusive(LocalDate.of(2020, 1, 31),
                LocalDate.of(2020, 1, 31), UserTestData.USER_ID), SecurityUtil.authUserCaloriesPerDay()));
        Assertions.assertEquals(responseJson, expectedResponseJson);
    }

    @Test
    void getBetweenOptional() throws Exception {
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL + "filterOptional")
                .param("startDate", "2020-01-31")
                .param("startTime", "00:00:00.000")
                .param("endDate", "2020-01-31")
                .param("endTime", "23:59:00.000"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andReturn();
        String responseJson = result.getResponse().getContentAsString();
        String expectedResponseJson = JsonUtil.writeValue(MealsUtil.getTos(service.getBetweenInclusive(LocalDate.of(2020, 1, 31),
                LocalDate.of(2020, 1, 31), UserTestData.USER_ID), SecurityUtil.authUserCaloriesPerDay()));
        Assertions.assertEquals(responseJson, expectedResponseJson);
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andExpect(result -> Assertions.assertEquals(result.getResponse().getContentAsString(),
                        JsonUtil.writeValue(MealsUtil.getTos(MealTestData.MEALS, SecurityUtil.authUserCaloriesPerDay()))));
    }
}
