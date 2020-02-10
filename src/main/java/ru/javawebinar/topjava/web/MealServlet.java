package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.FileStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    //this is hardcode
    private static final String pathToFileStorage = "C://123/file_storage";
    private static int caloriesPerDay = 2000;
    private static final FileStorage fileStorage = new FileStorage(pathToFileStorage);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");
        List<MealTo> mealsTo = MealsUtil.filteredByStreams(
                fileStorage.getAllMeals(), LocalTime.MIN, LocalTime.MAX, caloriesPerDay);
        String action = req.getParameter("action");
        if (action != null) {
            if (action.equalsIgnoreCase("delete")) {
                String id = req.getParameter("ID");
                if (id != null) {
                    fileStorage.delete(Integer.parseInt(id));

                    resp.sendRedirect("/meals");
                }
            } else if (action.equalsIgnoreCase("update")) {
                String id = req.getParameter("ID");
                if (id != null) {
                    req.getRequestDispatcher("update.jsp").forward(req, resp);
                }
            }
        }
        req.setAttribute("mealsList", mealsTo);
        req.getRequestDispatcher("meals.jsp").forward(req, resp);

    }
}
