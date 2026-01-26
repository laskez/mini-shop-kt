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

@WebServlet("/shop/update-cart")
public class UpdateCartServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(UpdateCartServlet.class);
    private final ProductDao productDao = new ProductDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String action = req.getParameter("action");
        String idParam = req.getParameter("id");

        HttpSession session = req.getSession();
        ServletContext context = getServletContext();

        if ("clear".equals(action)) {
            synchronized (session) {
                @SuppressWarnings("unchecked")
                Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

                if (cart != null) {
                    @SuppressWarnings("unchecked")
                    ConcurrentHashMap<Long, Product> catalogMap =
                            (ConcurrentHashMap<Long, Product>) context.getAttribute("catalogMap");

                    for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
                        Long productId = entry.getKey();
                        Integer quantity = entry.getValue();

                        if (catalogMap != null && catalogMap.containsKey(productId)) {
                            Product product = catalogMap.get(productId);
                            synchronized (product) {
                                product.increaseStock(quantity);
                                catalogMap.put(productId, product);
                            }
                        }

                        productDao.increaseStock(productId, quantity);
                    }

                    session.removeAttribute("cart");
                    logger.info("Корзина очищена. Все товары возвращены на склад.");
                }
            }
            resp.sendRedirect(req.getContextPath() + "/shop?msg=cleared");
            return;
        }

        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/shop?error=no-id");
            return;
        }

        long productId = Long.parseLong(idParam);

        synchronized (session) {
            @SuppressWarnings("unchecked")
            Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

            if (cart == null || !cart.containsKey(productId)) {
                resp.sendRedirect(req.getContextPath() + "/shop");
                return;
            }

            Integer currentQty = cart.get(productId);

            if ("remove".equals(action)) {
                @SuppressWarnings("unchecked")
                ConcurrentHashMap<Long, Product> catalogMap =
                        (ConcurrentHashMap<Long, Product>) context.getAttribute("catalogMap");

                if (catalogMap != null && catalogMap.containsKey(productId)) {
                    Product product = catalogMap.get(productId);
                    synchronized (product) {
                        product.increaseStock(currentQty);
                        catalogMap.put(productId, product);
                    }
                }

                productDao.increaseStock(productId, currentQty);
                cart.remove(productId);

                if (cart.isEmpty()) {
                    session.removeAttribute("cart");
                } else {
                    session.setAttribute("cart", cart);
                }

                logger.info("Товар ID {} удален из корзины. Возвращено {} шт.", productId, currentQty);
            }
        }

        resp.sendRedirect(req.getContextPath() + "/shop");
    }
}