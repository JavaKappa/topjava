package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.FileStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private static final String MEALS_LIST = "meals.jsp";
    private static final String EDIT_MEAL = "update.jsp";

    //this is hardcode
    public static final String pathToFileStorage = "C://123/file_storage";
    private static final Storage storage = new FileStorage(pathToFileStorage);
    private static final LocalTime startTime = LocalTime.MIN;
    private static final LocalTime endTime = LocalTime.MAX;
    private static int caloriesPerDay = 2000;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");
        req.setCharacterEncoding("UTF-8");
        String forward = "";
        String action = req.getParameter("action");
        if (action != null) {

            if (action.equalsIgnoreCase("delete")) {
                int mealId = Integer.parseInt(req.getParameter("id"));
                storage.delete(mealId);
                req.setAttribute("mealsList", MealsUtil.filteredByStreams(
                        storage.getAllMeals(), startTime, endTime, caloriesPerDay));
                forward = MEALS_LIST;
            } else if (action.equalsIgnoreCase("edit")) {
                int mealId = Integer.parseInt(req.getParameter("id"));
                Meal meal = storage.load(mealId);
                forward = EDIT_MEAL;
                req.setAttribute("meal", meal);
                req.removeAttribute("action");
                req.removeAttribute("id");
//                req.getRequestDispatcher(forward).forward(req, resp);

            } else if (action.equalsIgnoreCase("add")) {
                forward = EDIT_MEAL;
            }
        } else {
            forward = MEALS_LIST;
        }

        req.setAttribute("mealsList", MealsUtil.filteredByStreams(
                storage.getAllMeals(), startTime, endTime, caloriesPerDay));
        req.getRequestDispatcher(forward).forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String stringDate = req.getParameter("date");
        LocalDateTime date = LocalDateTime.parse(stringDate);
        String description = req.getParameter("description");
        int cal = Integer.parseInt(req.getParameter("cal"));
        String id = req.getParameter("id");

        if (id.isEmpty()) {
            storage.save(new Meal(date, description, cal));
        } else {
            storage.update(new Meal(Integer.parseInt(id), date, description, cal));
        }
        resp.sendRedirect("meals");
    }
}

