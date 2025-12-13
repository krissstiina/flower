package com.example.demo.Dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Relation(collectionRelation = "orders", itemRelation = "order")
public class OrderResponse extends RepresentationModel<OrderResponse> {

    private final Long id;
    private final BouquetResponse bouquet;
    private final Integer quantity;
    private final BigDecimal price;
    private final String status;
    private final String deliveryAddress;
    private final LocalDateTime orderDate;
    private final LocalDateTime deliveryDate;
    private final LocalDateTime createdAt;

    public OrderResponse(Long id, BouquetResponse bouquet, Integer quantity, BigDecimal price,
                         String status, String deliveryAddress, LocalDateTime orderDate,
                         LocalDateTime deliveryDate, LocalDateTime createdAt) {
        this.id = id;
        this.bouquet = bouquet;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.deliveryAddress = deliveryAddress;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public BouquetResponse getBouquet() {
        return bouquet;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OrderResponse that = (OrderResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(bouquet, that.bouquet) &&
                Objects.equals(quantity, that.quantity) && Objects.equals(price, that.price) &&
                Objects.equals(status, that.status) && Objects.equals(deliveryAddress, that.deliveryAddress) &&
                Objects.equals(orderDate, that.orderDate) && Objects.equals(deliveryDate, that.deliveryDate) &&
                Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, bouquet, quantity, price, status,
                deliveryAddress, orderDate, deliveryDate, createdAt);
    }
}

