package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private ConfigurableApplicationContext ctx;
    private MealRestController mealRestController;

    {
        ctx = new AnnotationConfigApplicationContext("spring/spring-app.xml");
        mealRestController = ctx.getBean(MealRestController.class);
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setFilter(request);

        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
                request.setAttribute("meal", mealRestController.create());
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "update":
                request.setAttribute("meal", mealRestController.get(getId(request)));
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        mealRestController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (meal.isNew()) {
            log.info("Create{}", meal);
            mealRestController.create(meal);
        } else {
            log.info("Update{}", meal);
            mealRestController.update(meal);
        }
        response.sendRedirect("meals");
    }

    public void setFilter(HttpServletRequest request) {
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String startTimeStr = request.getParameter("startTime");
        String endTimeStr = request.getParameter("endTime");

        if (startDateStr != null && !startDateStr.isEmpty()) {
            LocalDate startDate = LocalDate.parse(startDateStr);
            SecurityUtil.setStartDate(startDate);
        } else SecurityUtil.setStartDate(LocalDate.MIN);
        if (endDateStr != null && !endDateStr.isEmpty()) {
            LocalDate endDate = LocalDate.parse(endDateStr);
            SecurityUtil.setEndDate(endDate);
        } else SecurityUtil.setEndDate(LocalDate.MAX);
        if (startTimeStr != null && !startTimeStr.isEmpty()) {
            LocalTime startTime = LocalTime.parse(startTimeStr);
            SecurityUtil.setStartTime(startTime);
        } else SecurityUtil.setStartTime(LocalTime.MIN);
        if (endTimeStr != null && !endTimeStr.isEmpty()) {
            LocalTime endTime = LocalTime.parse(endTimeStr);
            SecurityUtil.setEndTime(endTime);
        } else SecurityUtil.setEndTime(LocalTime.MAX);
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
