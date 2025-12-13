package com.example.demo.Dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Relation(collectionRelation = "flowers", itemRelation = "flower")
public class FlowerResponse extends RepresentationModel<FlowerResponse> {

    private final Long id;
    private final String name;
    private final String color;
    private final BigDecimal price;
    private final Integer stockQuantity;
    private final String description;
    private final LocalDateTime createdAt;

    public FlowerResponse(Long id, String name, String color, BigDecimal price,
                          Integer stockQuantity, String description, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlowerResponse that = (FlowerResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) &&
                Objects.equals(color, that.color) && Objects.equals(price, that.price) &&
                Objects.equals(stockQuantity, that.stockQuantity) &&
                Objects.equals(description, that.description) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, color, price, stockQuantity, description, createdAt);
    }
}