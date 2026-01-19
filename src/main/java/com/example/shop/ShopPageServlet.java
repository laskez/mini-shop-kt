package com.example.shop;

import com.example.model.Product;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/shop")
public class ShopPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var catalogMap = (ConcurrentHashMap<Long, Product>) getServletContext().getAttribute("catalog");

        // для JSP: список товаров
        req.setAttribute("catalog", catalogMap.values());

        // корзина из сессии: productId -> qty
        HttpSession session = req.getSession();
        Map<Long, Integer> cartMap = (Map<Long, Integer>) session.getAttribute("cart");

        // для JSP: "читаемый" список строк
        var cartView = new ArrayList<String>();
        if (cartMap != null) {
            for (var e : cartMap.entrySet()) {
                Product p = catalogMap.get(e.getKey());
                if (p != null) cartView.add(p.getName() + " x " + e.getValue());
            }
        }
        req.setAttribute("cart", cartView);

        try {
            RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/shop.jsp");
            rd.forward(req, resp);
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }
}
