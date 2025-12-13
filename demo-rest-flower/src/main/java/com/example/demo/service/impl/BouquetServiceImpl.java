package com.example.demo.service.impl;

import com.example.demo.Dto.*;
import com.example.demo.exseption.InsufficientStockException;
import com.example.demo.exseption.ResourceNotFoundException;
import com.example.demo.model.Bouquet;
import com.example.demo.model.BouquetFlower;
import com.example.demo.model.Flower;
import com.example.demo.repository.BouquetRepository;
import com.example.demo.service.BouquetService;
import com.example.demo.service.FlowerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BouquetServiceImpl implements BouquetService {
    private final BouquetRepository storage;
    private final FlowerService flowerService;

    public BouquetServiceImpl(FlowerService flowerService, BouquetRepository storage) {
        this.flowerService = flowerService;
        this.storage = storage;
    }

    @Override
    public List<BouquetResponse> findAll(){
        return storage.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public BouquetResponse create(BouquetRequest request) {
        validateFlowersAvailability(request.flowers(), request.stockQuantity());

        BigDecimal price = calculateCostPrice(request.flowers());

        Bouquet bouquet = new Bouquet();
        bouquet.setName(request.name());
        bouquet.setPrice(price);
        bouquet.setStockQuantity(request.stockQuantity());
        bouquet.setCreatedAt(LocalDateTime.now());

        Bouquet savedBouquet = storage.save(bouquet);

        List<BouquetFlower> bouquetFlowers = request.flowers().stream()
                .map(item -> {
                    Flower flower = flowerService.findEntityById(item.flowerId());
                    return new BouquetFlower(savedBouquet, flower, item.quantity());
                }).collect(Collectors.toList());

        savedBouquet.setFlowers(bouquetFlowers);

        reserveFlowers(request.flowers(), request.stockQuantity());

        Bouquet finalBouquet = storage.save(savedBouquet);
        return toResponse(finalBouquet);
    }

    private void reserveFlowers(List<BouquetFlowerItem> flowers, int bouquetQuantity) {
        for (BouquetFlowerItem item : flowers) {
            Flower flower = flowerService.findEntityById(item.flowerId());
            int reservedQuantity = item.quantity() * bouquetQuantity;

            if (flower.getStockQuantity() < reservedQuantity) {
                throw new InsufficientStockException("Flower", flower.getId(), flower.getStockQuantity(), reservedQuantity);
            }

            flower.setStockQuantity(flower.getStockQuantity() - reservedQuantity);

            flowerService.saveFlower(flower);
        }
    }

    private void validateFlowersAvailability(List<BouquetFlowerItem> flowers, int bouquetQuantity) {
        for (BouquetFlowerItem item : flowers) {
            Flower flower = flowerService.findEntityById(item.flowerId());
            int requiredQuantity = item.quantity() * bouquetQuantity;
            if (flower.getStockQuantity() < requiredQuantity) {
                throw new InsufficientStockException("Flower", flower.getId(), flower.getStockQuantity(), requiredQuantity);
            }
        }
    }

    @Override
    public BouquetResponse findById(Long id) {
        Bouquet bouquet = storage.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bouquet", id));
        return toResponse(bouquet);
    }

    @Override
    @Transactional
    public BouquetResponse updateComposition(Long id, List<BouquetFlowerItem> flowers) {
        Bouquet existingBouquet = storage.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bouquet", id));

        validateFlowersAvailability(flowers, existingBouquet.getStockQuantity());

        returnFlowers(existingBouquet.getFlowers(), existingBouquet.getStockQuantity());

        BigDecimal newPrice = calculateCostPrice(flowers);

        List<BouquetFlower> newBouquetFlower = flowers.stream()
                .map(item -> {
                    Flower flower = flowerService.findEntityById(item.flowerId());
                    return new BouquetFlower(existingBouquet, flower, item.quantity());
                }).collect(Collectors.toList());

        existingBouquet.getFlowers().clear();
        existingBouquet.getFlowers().addAll(newBouquetFlower);
        existingBouquet.setPrice(newPrice);
        existingBouquet.setUpdatedAt(LocalDateTime.now());

        reserveFlowers(flowers, existingBouquet.getStockQuantity());

        Bouquet updatedBouquet = storage.save(existingBouquet);
        return toResponse(updatedBouquet);
    }

    private void returnFlowers(List<BouquetFlower> bouquetFlowers, int bouquetQuantity) {
        for (BouquetFlower bouquetFlower : bouquetFlowers) {
            Flower flower = bouquetFlower.getFlower();
            int returnedQuantity = bouquetFlower.getQuantity() * bouquetQuantity;
            flower.setStockQuantity(flower.getStockQuantity() + returnedQuantity);
            flowerService.saveFlower(flower);
        }
    }

    @Override
    @Transactional
    public BouquetResponse update(Long id, UpdateBouquetRequest request) {
        Bouquet existingBouquet = storage.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bouquet", id));

        int oldStockQuantity = existingBouquet.getStockQuantity();
        int newStockQuantity = request.stockQuantity();

        if(oldStockQuantity != newStockQuantity){
            int differenceQuantity = newStockQuantity - oldStockQuantity;

            if(differenceQuantity > 0 ){
                List<BouquetFlowerItem> flowerItems = existingBouquet.getFlowers().stream()
                        .map(item -> new BouquetFlowerItem(item.getFlower().getId(), item.getQuantity()))
                        .collect(Collectors.toList());
                reserveFlowers(flowerItems, differenceQuantity);
            } else {
                returnFlowers(existingBouquet.getFlowers(), Math.abs(differenceQuantity));
            }
        }

        existingBouquet.setName(request.name());
        existingBouquet.setStockQuantity(request.stockQuantity());
        existingBouquet.setUpdatedAt(LocalDateTime.now());

        Bouquet updateBouquet = storage.save(existingBouquet);
        return toResponse(updateBouquet);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Bouquet bouquet = storage.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bouquet", id));

        boolean hasActiveOrders = !bouquet.getOrders().isEmpty();

        if (hasActiveOrders) {
            throw new IllegalStateException("Cannot delete bouquet with id " + id +
                    " because it has active orders");
        }

        returnFlowers(bouquet.getFlowers(), bouquet.getStockQuantity());
        storage.delete(bouquet);
    }

    @Override
    public Bouquet findEntityById(Long id) {
        return storage.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bouquet", id));
    }

    @Override
    public void saveBouquet(Bouquet bouquet) {
        storage.save(bouquet);
    }

    private List<BouquetFlowerResponse> mapToBouquetFlowerResponse(List<BouquetFlower> bouquetFlowers) {
        return bouquetFlowers.stream()
                .map(bf -> {
                    Flower flower = bf.getFlower();
                    return new BouquetFlowerResponse(
                            flower.getId(),
                            flower.getName(),
                            flower.getColor(),
                            bf.getQuantity(),
                            flower.getPrice(),
                            flower.getPrice().multiply(BigDecimal.valueOf(bf.getQuantity()))
                    );
                })
                .collect(Collectors.toList());
    }

    private void validateFlowersAvailability(List<BouquetFlowerItem> flowers) {
        for (BouquetFlowerItem item : flowers) {
            Flower flower = flowerService.findEntityById(item.flowerId());
            if (flower.getStockQuantity() < item.quantity()) {
                throw new InsufficientStockException("Flower", flower.getId(), flower.getStockQuantity(), item.quantity());
            }
        }
    }

    private BigDecimal calculateCostPrice(List<BouquetFlowerItem> flowers) {
        BigDecimal total = BigDecimal.ZERO;
        for (BouquetFlowerItem item : flowers) {
            Flower flower = flowerService.findEntityById(item.flowerId());
            total = total.add(flower.getPrice().multiply(BigDecimal.valueOf(item.quantity())));
        }
        return total;
    }

    @Override
    @Transactional
    public BouquetResponse updateStock(Long id, Integer stockQuantity) {
        Bouquet existingBouquet = storage.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bouquet", id));

        int oldStockQuantity = existingBouquet.getStockQuantity();

        if (oldStockQuantity != stockQuantity) {
            int quantityDifference = stockQuantity - oldStockQuantity;

            if (quantityDifference > 0) {
                List<BouquetFlowerItem> flowerItems = existingBouquet.getFlowers().stream()
                        .map(bf -> new BouquetFlowerItem(bf.getFlower().getId(), bf.getQuantity()))
                        .collect(Collectors.toList());
                reserveFlowers(flowerItems, quantityDifference);
            } else {
                returnFlowers(existingBouquet.getFlowers(), Math.abs(quantityDifference));
            }
        }
        existingBouquet.setStockQuantity(stockQuantity);
        existingBouquet.setUpdatedAt(LocalDateTime.now());

        Bouquet updatedBouquet = storage.save(existingBouquet);
        return toResponse(updatedBouquet);
    }

    private BouquetResponse toResponse(Bouquet bouquet) {
        return new BouquetResponse(
                bouquet.getId(),
                bouquet.getName(),
                bouquet.getPrice(),
                bouquet.getStockQuantity(),
                mapToBouquetFlowerResponse(bouquet.getFlowers()),
                bouquet.getCreatedAt(),
                bouquet.getUpdatedAt() != null ? bouquet.getUpdatedAt() : bouquet.getCreatedAt()
        );
    }
}