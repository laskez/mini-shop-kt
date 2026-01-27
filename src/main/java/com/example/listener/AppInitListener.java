package com.example.listener;

import com.example.dao.ProductDao;
import com.example.model.Product;
import com.example.util.HibernateUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

@WebListener
public class AppInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Инициализация каталога товаров");

        try {
            HibernateUtil.getSessionFactory().openSession().close();
            System.out.println("Соединение с PostgreSQL установлено");

            initializeTestData();
            loadCatalogToApplicationScope(sce.getServletContext());

        } catch (Exception e) {
            System.err.println("Ошибка инициализации БД: " + e.getMessage());
        }
    }

    private void initializeTestData() {
        ProductDao dao = new ProductDao();

        if (dao.getAllProducts().isEmpty()) {
            System.out.println("Создание тестовых товаров");

            dao.saveProduct(new Product("Ноутбук", new BigDecimal("69999.99"), 6));
            dao.saveProduct(new Product("Мышь", new BigDecimal("3299.99"), 38));
            dao.saveProduct(new Product("Смартфон", new BigDecimal("54999.99"), 10));
            dao.saveProduct(new Product("Клавиатура", new BigDecimal("5449.99"), 19));
            dao.saveProduct(new Product("Наушники", new BigDecimal("11999.99"), 26));

            System.out.println("Создано 5 тестовых товаров");
        } else {
            System.out.println("Товары уже есть в БД");
        }
    }

    private void loadCatalogToApplicationScope(ServletContext context) {
        ProductDao dao = new ProductDao();
        var products = dao.getAllProducts();

        ConcurrentHashMap<Long, Product> catalogMap = new ConcurrentHashMap<>();

        for (Product product : products) {
            Product productCopy = new Product(
                    product.getName(),
                    product.getPrice(),
                    product.getStock()
            );
            productCopy.setId(product.getId());
            catalogMap.put(product.getId(), productCopy);
        }

        context.setAttribute("catalogMap", catalogMap);
        System.out.println("Загружено " + catalogMap.size() + " товаров в Application Scope");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Приложение завершено");
        HibernateUtil.shutdown();
    }
}