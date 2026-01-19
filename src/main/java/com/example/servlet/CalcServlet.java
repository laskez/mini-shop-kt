package com.example.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/calc")
public class CalcServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");

        HttpSession session = req.getSession();
        Integer sum = (Integer) session.getAttribute("sum");
        if (sum == null) sum = 0;

        resp.getWriter().println("<html><body>");
        resp.getWriter().println("<h1>Сумма: " + sum + "</h1>");

        resp.getWriter().println("""
            <form method='post' action='calc'>
              <input name='value' placeholder='число'>
              <button type='submit' name='action' value='add'>Прибавить</button>
              <button type='submit' name='action' value='reset'>Сброс</button>
            </form>
        """);

        resp.getWriter().println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        if ("reset".equals(action)) {
            req.getSession().invalidate();
            resp.sendRedirect(req.getContextPath() + "/calc");
            return;
        }

        HttpSession session = req.getSession();
        Integer sum = (Integer) session.getAttribute("sum");
        if (sum == null) sum = 0;

        int value = 0;
        try {
            value = Integer.parseInt(req.getParameter("value"));
        } catch (Exception ignored) {}

        sum += value;
        session.setAttribute("sum", sum);

        resp.sendRedirect(req.getContextPath() + "/calc");
    }
}
