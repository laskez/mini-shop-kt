package com.example.listener;

import com.example.model.Product;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

@WebListener
public class AppInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        var catalog = new ConcurrentHashMap<Long, Product>();

        catalog.put(1L, new Product(1, "Apple",  new BigDecimal("1.20"), 3));
        catalog.put(2L, new Product(2, "Banana", new BigDecimal("0.90"), 5));
        catalog.put(3L, new Product(3, "Orange", new BigDecimal("1.10"), 0));
        catalog.put(4L, new Product(4, "Milk",   new BigDecimal("2.30"), 7));
        catalog.put(5L, new Product(5, "Bread",  new BigDecimal("1.70"), 2));

        sce.getServletContext().setAttribute("catalog", catalog);
    }
}
