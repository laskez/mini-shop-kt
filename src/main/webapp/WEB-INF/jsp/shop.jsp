<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Мини-Магазин</title>
</head>
<body>

<h1>Мини-Магазин</h1>

<c:if test="${not empty param.error}">
    <p style="color:red;">
        <c:choose>
            <c:when test="${param.error == 'nostock'}">Товара нет на складе</c:when>
            <c:otherwise>Неизвестная ошибка</c:otherwise>
        </c:choose>
    </p>
</c:if>

<h2>Каталог</h2>
<ul>
    <c:forEach var="p" items="${catalog}">
        <li>
                ${p.name} — ${p.price} — остаток: ${p.stock}

            <c:choose>
                <c:when test="${p.stock > 0}">
                    <form method="post" action="${pageContext.request.contextPath}/shop/add" style="display:inline;">
                        <input type="hidden" name="id" value="${p.id}">
                        <button type="submit">Добавить</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <b>Нет в наличии</b>
                </c:otherwise>
            </c:choose>
        </li>
    </c:forEach>
</ul>

<hr/>

<h2>Корзина</h2>

<c:if test="${empty cart}">
    <p>Корзина пуста</p>
</c:if>

<c:if test="${not empty cart}">
    <ul>
        <c:forEach var="row" items="${cart}">
            <li>${row}</li>
        </c:forEach>
    </ul>
</c:if>

<p><a href="${pageContext.request.contextPath}/">Назад в меню</a></p>

</body>
</html>
