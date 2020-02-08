package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.MealTo;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    List<MealTo> mealsTo = Arrays.asList(
            new MealTo(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500, false),
            new MealTo(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000, false),
            new MealTo(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500, false),
            new MealTo(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100, true),
            new MealTo(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000, true),
            new MealTo(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500, true),
            new MealTo(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410, true)
    );

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");
        req.setAttribute("mealsList", mealsTo);
        req.getRequestDispatcher("meals.jsp").forward(req, resp);
    }
}
