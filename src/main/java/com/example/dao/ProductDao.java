package com.example.dao;

import com.example.model.Product;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductDao {

    private static final Logger logger = LoggerFactory.getLogger(ProductDao.class);

    public List<Product> getAllProducts() {
        logger.debug("Получение всех товаров");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Product ORDER BY name", Product.class).list();
        }
    }

    public Product getProductById(Long id) {
        logger.debug("Поиск товара по ID: {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Product.class, id);
        }
    }

    public void saveProduct(Product product) {
        logger.info("Сохранение товара: {}", product.getName());
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(product);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Ошибка сохранения товара", e);
            throw e;
        }
    }

    public void updateProduct(Product product) {
        logger.info("Обновление товара: {}", product.getName());
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(product);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Ошибка обновления товара", e);
            throw e;
        }
    }

    public boolean reserveProduct(Long productId, int quantity) {
        logger.debug("Резервирование товара ID: {}", productId);
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Product product = session.get(Product.class, productId);
            if (product == null) {
                logger.warn("Товар ID: {} не найден", productId);
                tx.rollback();
                return false;
            }

            if (product.getStock() < quantity) {
                logger.warn("Недостаточно товара ID: {}", productId);
                tx.rollback();
                return false;
            }

            product.setStock(product.getStock() - quantity);
            session.update(product);
            tx.commit();
            logger.info("Товар ID: {} зарезервирован", productId);
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Ошибка резервирования", e);
            return false;
        }
    }

    public boolean decreaseStock(Long productId, int quantity) {
        logger.debug("Уменьшение запаса товара ID: {} на {}", productId, quantity);
        return reserveProduct(productId, quantity);
    }

    public boolean increaseStock(Long productId, int quantity) {
        logger.debug("Возврат товара ID: {} в количестве {}", productId, quantity);
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Product product = session.get(Product.class, productId);
            if (product == null) {
                logger.warn("Товар ID: {} не найден при возврате", productId);
                tx.rollback();
                return false;
            }

            product.setStock(product.getStock() + quantity);
            session.update(product);
            tx.commit();
            logger.info("Товар ID: {} возвращен. Новый запас: {}", productId, product.getStock());
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Ошибка возврата товара", e);
            return false;
        }
    }

    public int getAvailableStock(Long productId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Product product = session.get(Product.class, productId);
            return product != null ? product.getStock() : 0;
        }
    }
}