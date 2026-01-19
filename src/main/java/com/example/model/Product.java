package com.example.model;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class Product {
    private final long id;
    private final String name;
    private final BigDecimal price;
    private final AtomicInteger stock;

    public Product(long id, String name, BigDecimal price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = new AtomicInteger(stock);
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock.get(); }

    public boolean tryReserveOne() {
        while (true) {
            int current = stock.get();
            if (current <= 0) return false;
            if (stock.compareAndSet(current, current - 1)) return true;
        }
    }
}
