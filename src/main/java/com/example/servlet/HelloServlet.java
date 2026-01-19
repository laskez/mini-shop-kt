package com.example.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().println("<html><body>");
        resp.getWriter().println("<h1>Здравствуйте! Добро пожаловать в Мини-Магазин</h1>");
        resp.getWriter().println("</body></html>");
    }
}
