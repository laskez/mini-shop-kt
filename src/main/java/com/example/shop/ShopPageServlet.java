package com.example.shop;

import com.example.dao.ProductDao;
import com.example.model.Product;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/shop")
public class ShopPageServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ServletContext context = getServletContext();
        @SuppressWarnings("unchecked")
        ConcurrentHashMap<Long, Product> catalogMap =
                (ConcurrentHashMap<Long, Product>) context.getAttribute("catalogMap");

        if (catalogMap == null || catalogMap.isEmpty()) {
            catalogMap = loadCatalogFromDB();
            context.setAttribute("catalogMap", catalogMap);
        }

        List<Product> products = new ArrayList<>(catalogMap.values());
        products.sort(Comparator.comparing(Product::getName));
        req.setAttribute("catalog", products);

        HttpSession session = req.getSession();
        Map<Long, Integer> cartMap = (Map<Long, Integer>) session.getAttribute("cart");

        List<CartItem> cartItems = new ArrayList<>();
        BigDecimal totalCartPrice = BigDecimal.ZERO;

        if (cartMap != null && !cartMap.isEmpty()) {
            for (Map.Entry<Long, Integer> entry : cartMap.entrySet()) {
                Product product = catalogMap.get(entry.getKey());
                if (product != null) {
                    int quantity = entry.getValue();
                    BigDecimal itemTotal = product.getPrice()
                            .multiply(BigDecimal.valueOf(quantity));

                    cartItems.add(new CartItem(product, quantity, itemTotal));
                    totalCartPrice = totalCartPrice.add(itemTotal);
                }
            }

            cartItems.sort(Comparator.comparing(item -> item.getProduct().getName()));
        }

        req.setAttribute("cartItems", cartItems);
        req.setAttribute("totalCartPrice", totalCartPrice);

        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/jsp/shop.jsp");
        rd.forward(req, resp);
    }

    private ConcurrentHashMap<Long, Product> loadCatalogFromDB() {
        ConcurrentHashMap<Long, Product> catalogMap = new ConcurrentHashMap<>();
        List<Product> products = productDao.getAllProducts();

        for (Product product : products) {
            Product productCopy = new Product(
                    product.getName(),
                    product.getPrice(),
                    product.getStock()
            );
            productCopy.setId(product.getId());
            catalogMap.put(product.getId(), productCopy);
        }

        return catalogMap;
    }

    public static class CartItem {
        private final Product product;
        private final int quantity;
        private final BigDecimal totalPrice;

        public CartItem(Product product, int quantity, BigDecimal totalPrice) {
            this.product = product;
            this.quantity = quantity;
            this.totalPrice = totalPrice;
        }

        public Product getProduct() { return product; }
        public int getQuantity() { return quantity; }
        public BigDecimal getTotalPrice() { return totalPrice; }
    }
}