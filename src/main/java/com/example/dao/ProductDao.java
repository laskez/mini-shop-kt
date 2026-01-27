package com.example.dao;

import com.example.model.Product;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProductDao {

    public List<Product> getAllProducts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Product ORDER BY name", Product.class).list();
        }
    }

    public Product getProductById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Product.class, id);
        }
    }

    public void saveProduct(Product product) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(product);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void updateProduct(Product product) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(product);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public boolean reserveProduct(Long productId, int quantity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Product product = session.get(Product.class, productId);
            if (product == null) {
                tx.rollback();
                return false;
            }

            if (product.getStock() < quantity) {
                tx.rollback();
                return false;
            }

            product.setStock(product.getStock() - quantity);
            session.update(product);
            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    public boolean decreaseStock(Long productId, int quantity) {
        return reserveProduct(productId, quantity);
    }

    public boolean increaseStock(Long productId, int quantity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Product product = session.get(Product.class, productId);
            if (product == null) {
                tx.rollback();
                return false;
            }

            product.setStock(product.getStock() + quantity);
            session.update(product);
            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
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