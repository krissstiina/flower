package com.example.demo.model;


import jakarta.persistence.*;

@Entity
@Table(name = "bouquet_flowers")
public class BouquetFlower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bouquet_id", nullable = false)
    private Bouquet bouquet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flower_id", nullable = false)
    private Flower flower;

    @Column(nullable = false)
    private Integer quantity;

    public BouquetFlower() {
    }

    public BouquetFlower(Bouquet bouquet, Flower flower, Integer quantity) {
        this.bouquet = bouquet;
        this.flower = flower;
        this.quantity = quantity;
    }

    public Bouquet getBouquet() {
        return bouquet;
    }

    public Flower getFlower() {
        return flower;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setBouquet(Bouquet bouquet) {
        this.bouquet = bouquet;
    }

    public void setFlower(Flower flower) {
        this.flower = flower;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
