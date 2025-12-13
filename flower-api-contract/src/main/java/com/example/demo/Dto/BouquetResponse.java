package com.example.demo.Dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Relation(collectionRelation = "bouquets", itemRelation = "bouquet")
public class BouquetResponse extends RepresentationModel<BouquetResponse> {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Integer stockQuantity;
    private final List<BouquetFlowerResponse> flowers;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public BouquetResponse(Long id, String name, BigDecimal price, Integer stockQuantity,
                           List<BouquetFlowerResponse> flowers, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.flowers = flowers;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public List<BouquetFlowerResponse> getFlowers() {
        return flowers;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BouquetResponse that = (BouquetResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) &&
                Objects.equals(price, that.price) && Objects.equals(stockQuantity, that.stockQuantity) &&
                Objects.equals(flowers, that.flowers) && Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, price, stockQuantity, flowers, createdAt, updatedAt);
    }
}

