package com.example.demo.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bouquets")
public class Bouquet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "bouquet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BouquetFlower> flowers = new ArrayList<>();

    @OneToMany(mappedBy = "bouquet")
    private List<Order> orders = new ArrayList<>();

    public Bouquet() {
    }

    // Обновленный конструктор с updatedAt
    public Bouquet(String name, BigDecimal price, Integer stockQuantity,
                   LocalDateTime createdAt, LocalDateTime updatedAt,
                   List<BouquetFlower> flowers, List<Order> orders) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.flowers = flowers != null ? flowers : new ArrayList<>();
        this.orders = orders != null ? orders : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<BouquetFlower> getFlowers() {
        return flowers;
    }

    public void setFlowers(List<BouquetFlower> flowers) {
        this.flowers = flowers;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    // Вспомогательные методы для управления связями
    public void addFlower(BouquetFlower bouquetFlower) {
        flowers.add(bouquetFlower);
        bouquetFlower.setBouquet(this);
    }

    public void removeFlower(BouquetFlower bouquetFlower) {
        flowers.remove(bouquetFlower);
        bouquetFlower.setBouquet(null);
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setBouquet(this);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
        order.setBouquet(null);
    }

    @Override
    public String toString() {
        return "Bouquet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", flowersCount=" + (flowers != null ? flowers.size() : 0) +
                ", ordersCount=" + (orders != null ? orders.size() : 0) +
                '}';
    }
}