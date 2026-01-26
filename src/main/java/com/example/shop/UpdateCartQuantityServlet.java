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

@WebServlet("/shop/update-cart-quantity")
public class UpdateCartQuantityServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(UpdateCartQuantityServlet.class);
    private final ProductDao productDao = new ProductDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String action = req.getParameter("action");
        String idParam = req.getParameter("id");
        String quantityParam = req.getParameter("quantity");

        HttpSession session = req.getSession();
        ServletContext context = getServletContext();

        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/shop?error=no-id");
            return;
        }

        long productId = Long.parseLong(idParam);

        @SuppressWarnings("unchecked")
        ConcurrentHashMap<Long, Product> catalogMap =
                (ConcurrentHashMap<Long, Product>) context.getAttribute("catalogMap");

        if (catalogMap == null || !catalogMap.containsKey(productId)) {
            resp.sendRedirect(req.getContextPath() + "/shop");
            return;
        }

        Product product = catalogMap.get(productId);
        boolean updatePerformed = false;

        synchronized (session) {
            @SuppressWarnings("unchecked")
            Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

            if (cart == null || !cart.containsKey(productId)) {
                resp.sendRedirect(req.getContextPath() + "/shop");
                return;
            }

            Integer currentQty = cart.get(productId);
            int newQty = currentQty;

            if ("decrease".equals(action)) {
                if (currentQty > 1) {
                    newQty = currentQty - 1;
                    synchronized (product) {
                        product.increaseStock(1);
                        catalogMap.put(productId, product);
                    }
                    productDao.increaseStock(productId, 1);
                    updatePerformed = true;
                } else {
                    synchronized (product) {
                        product.increaseStock(currentQty);
                        catalogMap.put(productId, product);
                    }
                    productDao.increaseStock(productId, currentQty);
                    cart.remove(productId);
                    updatePerformed = true;
                }

            } else if ("increase".equals(action)) {
                if (currentQty < 10) {
                    synchronized (product) {
                        if (product.getStock() > 0) {
                            if (product.decreaseStock(1)) {
                                catalogMap.put(productId, product);
                                newQty = currentQty + 1;
                                productDao.decreaseStock(productId, 1);
                                updatePerformed = true;
                            }
                        }
                    }
                }

            } else if (quantityParam != null && !quantityParam.trim().isEmpty()) {
                try {
                    int requestedQty = Integer.parseInt(quantityParam);

                    if (requestedQty < 1) requestedQty = 1;
                    if (requestedQty > 10) requestedQty = 10;

                    if (requestedQty != currentQty) {
                        int difference = requestedQty - currentQty;

                        if (difference > 0) {
                            synchronized (product) {
                                if (product.getStock() >= difference) {
                                    if (product.decreaseStock(difference)) {
                                        catalogMap.put(productId, product);
                                        newQty = requestedQty;
                                        productDao.decreaseStock(productId, difference);
                                        updatePerformed = true;
                                    }
                                } else {
                                    int available = product.getStock();
                                    if (available > 0) {
                                        product.decreaseStock(available);
                                        catalogMap.put(productId, product);
                                        newQty = currentQty + available;
                                        productDao.decreaseStock(productId, available);
                                        updatePerformed = true;
                                    }
                                }
                            }
                        } else {
                            synchronized (product) {
                                product.increaseStock(-difference);
                                catalogMap.put(productId, product);
                                newQty = requestedQty;
                                productDao.increaseStock(productId, -difference);
                                updatePerformed = true;
                            }
                        }
                    }

                } catch (NumberFormatException e) {
                    logger.warn("Некорректное количество: {}", quantityParam);
                }
            }

            if (updatePerformed) {
                if (newQty > 0) {
                    cart.put(productId, newQty);
                } else {
                    cart.remove(productId);
                }

                if (cart.isEmpty()) {
                    session.removeAttribute("cart");
                } else {
                    session.setAttribute("cart", cart);
                }
            }
        }

        resp.sendRedirect(req.getContextPath() + "/shop");
    }
}