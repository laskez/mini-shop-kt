package com.example.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final List<String> users = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");

        resp.getWriter().println("""
            <!doctype html>
            <html lang="ru">
            <head>
              <meta charset="UTF-8">
              <title>Регистрация</title>
            </head>
            <body>
            <h1>Регистрация</h1>

            <form method="post" action="register">
              <div>Имя: <input name="name"></div>
              <div>Email: <input name="email"></div>
              <div>Пароль: <input type="password" name="password"></div>
              <button type="submit">Зарегистрироваться</button>
            </form>

            <p><a href="/">Назад</a></p>
            </body>
            </html>
        """);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String name = req.getParameter("name");
        String email = req.getParameter("email");

        if (email == null || !email.contains("@")) {
            resp.getWriter().println("<html><body>");
            resp.getWriter().println("<h2>Ошибка: email должен содержать @</h2>");
            resp.getWriter().println("<a href='register'>Назад</a>");
            resp.getWriter().println("</body></html>");
            return;
        }

        users.add(name + " <" + email + ">");
        resp.sendRedirect(req.getContextPath() + "/success");
    }
}
