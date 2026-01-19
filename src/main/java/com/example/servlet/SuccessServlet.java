package com.example.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/success")
public class SuccessServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().println("<html><body>");
        resp.getWriter().println("<h1>Успех! Регистрация принята.</h1>");
        resp.getWriter().println("<a href='register.html'>Назад к форме</a>");
        resp.getWriter().println("</body></html>");
    }
}
