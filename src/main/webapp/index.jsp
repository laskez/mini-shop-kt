<%@ page contentType="text/html; charset=UTF-8" %>
<%
    // редирект на магазин
    String contextPath = request.getContextPath();
    response.sendRedirect(contextPath + "/shop");
%>