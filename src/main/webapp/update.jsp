<%@ page import="ru.javawebinar.topjava.model.Meal" %><%--
  Created by IntelliJ IDEA.
  User: qwark
  Date: 09.02.2020
  Time: 22:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update meal</title>
</head>
<body>
    <p>Update Meal</p>
    <form action="meals" method="post">
        <p>ID:</p><input readonly name="id" value="${meal.id}">
        <div><p>Дата время: </p><input placeholder="2020-01-01 23:33" name="date" value="${meal.date} ${meal.getTime()}"></div>
        <div><p>Описание: </p><input placeholder="Торт" name="description" value="${meal.description}"></div>
        <div><p>Каллорийность: </p><input placeholder="200" name="cal" value="${meal.calories}"></div>
        <%request.setAttribute("meal", request.getAttribute("meal"));%>
        <button type="submit">Сохранить</button>
        <button type="button">Отмена</button>
    </form>
</body>
</html>
