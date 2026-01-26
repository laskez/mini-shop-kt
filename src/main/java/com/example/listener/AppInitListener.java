package com.example.listener;

import com.example.dao.ProductDao;
import com.example.model.Product;
import com.example.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

@WebListener
public class AppInitListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(AppInitListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Инициализация каталога товаров");

        try {
            HibernateUtil.getSessionFactory().openSession().close();
            logger.info("Соединение с PostgreSQL установлено");

            initializeTestData();

            loadCatalogToApplicationScope(sce.getServletContext());

        } catch (Exception e) {
            logger.error("Ошибка инициализации БД", e);
        }
    }

    private void initializeTestData() {
        ProductDao dao = new ProductDao();

        if (dao.getAllProducts().isEmpty()) {
            logger.info("Создание тестовых товаров в БД");

            dao.saveProduct(new Product("Ноутбук", new BigDecimal("69999.99"), 6));
            dao.saveProduct(new Product("Мышь", new BigDecimal("3299.99"), 38));
            dao.saveProduct(new Product("Смартфон", new BigDecimal("54999.99"), 10));
            dao.saveProduct(new Product("Клавиатура", new BigDecimal("5449.99"), 19));
            dao.saveProduct(new Product("Наушники", new BigDecimal("11999.99"), 26));

            logger.info("Создано 5 тестовых товаров в БД");
        } else {
            logger.info("Товары уже есть в БД");
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

        logger.info("Загружено {} товаров в Application Scope (ConcurrentHashMap)", catalogMap.size());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Приложение завершено");
        HibernateUtil.shutdown();
    }
}