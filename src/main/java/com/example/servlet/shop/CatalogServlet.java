package com.example.servlet.shop;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@WebServlet("/catalog")
public class CatalogServlet extends HttpServlet {

    private static final List<String> GOODS = List.of("Apple", "Banana", "Orange");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");

        // username из cookie
        String username = readCookie(req, "username");
        if (username == null) username = "Незнакомец";

        resp.getWriter().println("<html><body>");
        resp.getWriter().println("<div style='position:fixed;top:10px;right:10px;'>Привет, " + username + "!</div>");
        resp.getWriter().println("<h1>Каталог</h1>");

        // форма для установки имени в cookie
        resp.getWriter().println("""
            <form method='post' action='catalog'>
              <input name='username' placeholder='Ваше имя'>
              <button type='submit' name='action' value='setname'>Запомнить имя</button>
            </form>
            <hr>
        """);

        for (String g : GOODS) {
            String enc = URLEncoder.encode(g, StandardCharsets.UTF_8);
            resp.getWriter().println("<div>");
            resp.getWriter().println(g + " ");
            resp.getWriter().println("<a href='catalog?add=" + enc + "'>В корзину</a>");
            resp.getWriter().println("</div>");
        }

        resp.getWriter().println("<hr><a href='cart'>Перейти в корзину</a>");
        resp.getWriter().println("</body></html>");

        // добавление товара по query-параметру
        String add = req.getParameter("add");
        if (add != null && !add.isBlank()) {
            HttpSession session = req.getSession();
            List<String> cart = (List<String>) session.getAttribute("cart");
            if (cart == null) {
                cart = new ArrayList<>();
                session.setAttribute("cart", cart);
            }
            cart.add(add);

            // чтобы F5 не добавлял повторно — редирект
            resp.sendRedirect(req.getContextPath() + "/catalog");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        if ("setname".equals(action)) {
            String username = req.getParameter("username");
            if (username != null && !username.isBlank()) {
                Cookie c = new Cookie("username", username);
                c.setMaxAge(60 * 60 * 24 * 30); // 30 дней
                c.setPath("/");
                resp.addCookie(c);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/catalog");
    }

    private String readCookie(HttpServletRequest req, String name) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;
        for (Cookie c : cookies) {
            if (name.equals(c.getName())) return c.getValue();
        }
        return null;
    }
}
