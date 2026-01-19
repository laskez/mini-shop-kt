package com.example.shop;

import com.example.model.Product;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/shop/add")
public class MainServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        long productId;
        try {
            productId = Long.parseLong(req.getParameter("id"));
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/shop");
            return;
        }

        var catalog = (ConcurrentHashMap<Long, Product>) getServletContext().getAttribute("catalog");
        Product product = catalog.get(productId);
        if (product == null) {
            resp.sendRedirect(req.getContextPath() + "/shop");
            return;
        }

        // если stock 0 — не добавляем
        if (!product.tryReserveOne()) {
            resp.sendRedirect(req.getContextPath() + "/shop?error=nostock");
            return;
        }

        HttpSession session = req.getSession();
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ConcurrentHashMap<>();
            session.setAttribute("cart", cart);
        }

        cart.merge(productId, 1, Integer::sum);

        // PRG
        resp.sendRedirect(req.getContextPath() + "/shop");
    }
}
