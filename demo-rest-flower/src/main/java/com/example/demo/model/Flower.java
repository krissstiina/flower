package com.example.demo.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "flowers")
public class Flower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "flower", cascade = CascadeType.ALL)
    private List<BouquetFlower> bouquetFlowers = new ArrayList<>();

    public Flower() {
    }

    public Flower(List<BouquetFlower> bouquetFlowers, String color, LocalDateTime createdAt, String description, String name, BigDecimal price, Integer stockQuantity) {
        this.bouquetFlowers = bouquetFlowers;
        this.color = color;
        this.createdAt = createdAt;
        this.description = description;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public List<BouquetFlower> getBouquetFlowers() {
        return bouquetFlowers;
    }

    public String getColor() {
        return color;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setBouquetFlowers(List<BouquetFlower> bouquetFlowers) {
        this.bouquetFlowers = bouquetFlowers;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
