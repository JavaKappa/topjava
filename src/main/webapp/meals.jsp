<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="ru.javawebinar.topjava.util.DateTimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Meals List</title>
    <style>
        .normal {
            color: green;
            background: #dfffd0
        }

        .exceed {
            color: red;
            background: #ffeddd
        }
    </style>
</head>
<body>
<a href="meals?action=add"><IMG style="text-align: center" height="100"
                                src="http://s1.iconbird.com/ico/0912/ToolbarIcons/w256h2561346685474SymbolAdd.png"></a>
<table align="center" border="1" cellpadding="10" cellspacing="0" style="text-align: center">
    <thead>
    <tr>
        <th>ID</th>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th colspan="2">Действия</th>
    </tr>
    </thead>
    <c:forEach items="${mealsList}" var="meal">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr class="${meal.excess?'exceed':'normal'}">
            <td>${meal.ID}</td>
            <td><%=DateTimeUtil.toString(meal.getDateTime())%></td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td style="padding: 0px"><a href="meals?action=edit&id=${meal.ID}"><img height="50"
                                                                                    src="https://pngimage.net/wp-content/uploads/2018/05/edit-png-image-4.png"></a>
            </td>
            <td style="padding: 0px"><a href="meals?action=delete&id=${meal.ID}"><img height="50"
                                                                                      src="https://upload.wikimedia.org/wikipedia/commons/thumb/6/6d/Symbol_speedy_delete_vote.svg/180px-Symbol_speedy_delete_vote.svg.png"></a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
