<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Meals List</title>
</head>
<body>
<%--    <%request.getAttribute("ggii")%>--%>
<table align="center" border="1" cellpadding="10" cellspacing="0" style="text-align: center">
    <thead>
    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
    </tr>
    </thead>
    <c:forEach items="${mealsList}" var="meal">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <c:if test="${meal.excess}">
        <tr style="color: red;
                    background: #ffeddd">
            <td>${meal.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
        </tr>
        </c:if>
        <c:if test="${!meal.excess}">
            <tr style="color: green;
                        background: #dfffd0">
                <td>${meal.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
            </tr>
        </c:if>
    </c:forEach>
</table>
</body>
</html>
