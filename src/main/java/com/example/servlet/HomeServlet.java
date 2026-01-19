package com.example.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");

        resp.getWriter().println("""
            <!doctype html>
            <html lang="ru">
            <head>
              <meta charset="UTF-8">
              <title>Меню</title>
            </head>
            <body>
              <h1>Меню</h1>
              <ul>
                <li><a href="hello">Приветствие</a></li>
                <li><a href="register">Регистрация</a></li>
                <li><a href="shop">Магазин</a></li>
                <li><a href="calc">Калькулятор</a></li>
              </ul>
            </body>
            </html>
        """);
    }
}
