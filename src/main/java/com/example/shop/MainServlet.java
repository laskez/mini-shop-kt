package com.example.shop;

import com.example.dao.ProductDao;
import com.example.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/shop/add")
public class MainServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(MainServlet.class);
    private final ProductDao productDao = new ProductDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String productIdParam = req.getParameter("id");
        String quantityParam = req.getParameter("quantity");

        long productId;
        int quantityToAdd = 1;

        try {
            productId = Long.parseLong(productIdParam);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/shop?error=invalid");
            return;
        }

        if (quantityParam != null && !quantityParam.trim().isEmpty()) {
            try {
                quantityToAdd = Integer.parseInt(quantityParam);
                if (quantityToAdd <= 0) quantityToAdd = 1;
                if (quantityToAdd > 10) quantityToAdd = 10;
            } catch (NumberFormatException e) {
                quantityToAdd = 1;
            }
        }

        ServletContext context = getServletContext();
        @SuppressWarnings("unchecked")
        ConcurrentHashMap<Long, Product> catalogMap =
                (ConcurrentHashMap<Long, Product>) context.getAttribute("catalogMap");

        if (catalogMap == null) {
            resp.sendRedirect(req.getContextPath() + "/shop?error=catalog-not-loaded");
            return;
        }

        Product product = catalogMap.get(productId);
        if (product == null) {
            resp.sendRedirect(req.getContextPath() + "/shop?error=notfound");
            return;
        }

        HttpSession session = req.getSession();
        Map<Long, Integer> cart = null;

        synchronized (session) {
            @SuppressWarnings("unchecked")
            Map<Long, Integer> cartTemp = (Map<Long, Integer>) session.getAttribute("cart");
            cart = cartTemp;
        }

        if (cart != null) {
            int currentInCart = cart.getOrDefault(productId, 0);
            if (currentInCart >= 10) {
                resp.sendRedirect(req.getContextPath() + "/shop?error=limit&id=" + productId);
                return;
            }

            int maxCanAdd = Math.min(10 - currentInCart, product.getStock());
            if (quantityToAdd > maxCanAdd) {
                quantityToAdd = maxCanAdd;
                if (quantityToAdd <= 0) {
                    resp.sendRedirect(req.getContextPath() + "/shop?error=nostock&id=" + productId);
                    return;
                }
            }
        } else {
            if (quantityToAdd > product.getStock()) {
                quantityToAdd = product.getStock();
                if (quantityToAdd <= 0) {
                    resp.sendRedirect(req.getContextPath() + "/shop?error=nostock&id=" + productId);
                    return;
                }
            }
            if (quantityToAdd > 10) quantityToAdd = 10;
        }

        boolean stockUpdated = false;
        synchronized (product) {
            if (product.decreaseStock(quantityToAdd)) {
                stockUpdated = true;
                catalogMap.put(productId, product);
            }
        }

        if (!stockUpdated) {
            resp.sendRedirect(req.getContextPath() + "/shop?error=nostock&id=" + productId);
            return;
        }

        if (!productDao.decreaseStock(productId, quantityToAdd)) {
            synchronized (product) {
                product.increaseStock(quantityToAdd);
                catalogMap.put(productId, product);
            }
            resp.sendRedirect(req.getContextPath() + "/shop?error=stock-error");
            return;
        }

        synchronized (session) {
            @SuppressWarnings("unchecked")
            Map<Long, Integer> cartMap = (Map<Long, Integer>) session.getAttribute("cart");

            if (cartMap == null) {
                cartMap = new ConcurrentHashMap<>();
                session.setAttribute("cart", cartMap);
            }

            int currentQuantity = cartMap.getOrDefault(productId, 0);
            int newQuantity = currentQuantity + quantityToAdd;

            if (newQuantity > 10) {
                newQuantity = 10;
            }

            cartMap.put(productId, newQuantity);
            session.setAttribute("cart", cartMap);
        }

        logger.info("Товар ID {} добавлен в корзину ({} шт.). Остаток на складе: {}",
                productId, quantityToAdd, product.getStock());

        resp.sendRedirect(req.getContextPath() + "/shop?added=" + productId);
    }
}