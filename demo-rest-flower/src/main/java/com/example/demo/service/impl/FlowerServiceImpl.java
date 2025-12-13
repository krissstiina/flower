package com.example.demo.service.impl;

import com.example.demo.Dto.FlowerRequest;
import com.example.demo.Dto.FlowerResponse;
import com.example.demo.Dto.UpdateFlowerRequest;
import com.example.demo.config.RabbitMQConfig;
import com.example.demo.exseption.ResourceNotFoundException;
import com.example.demo.model.Flower;
import com.example.demo.service.FlowerService;
import com.example.demo.repository.FlowerRepository;
import org.example.events.FlowerStockLowEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class FlowerServiceImpl implements FlowerService {

    private final FlowerRepository flowerRepository;
    private final RabbitTemplate rabbitTemplate;
    private static final Integer LOW_STOCK_THRESHOLD = 10;

    public FlowerServiceImpl(FlowerRepository flowerRepository, RabbitTemplate rabbitTemplate) {
        this.flowerRepository = flowerRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public List<FlowerResponse> findAll() {
        return flowerRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public FlowerResponse findById(Long id) {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flower", id));
        return toResponse(flower);
    }

    @Override
    @Transactional(readOnly = true)
    public Flower findEntityById(Long id) {
        return flowerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flower", id));
    }

    @Override
    public FlowerResponse create(FlowerRequest request) {
        Flower flower = new Flower();
        flower.setName(request.name());
        flower.setColor(request.color());
        flower.setPrice(request.price());
        flower.setStockQuantity(request.stockQuantity());
        flower.setDescription(request.description());

        Flower savedFlower = flowerRepository.save(flower);

        checkLowStock(savedFlower);

        return toResponse(savedFlower);
    }

    @Override
    public FlowerResponse updateStockQuantity(Long id, Integer stockQuantity) {
        Flower existingFlower = flowerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flower", id));

        Integer oldStock = existingFlower.getStockQuantity();

        existingFlower.setStockQuantity(stockQuantity);

        Flower updatedFlower = flowerRepository.save(existingFlower);

        if (updatedFlower.getStockQuantity() <= LOW_STOCK_THRESHOLD && oldStock > LOW_STOCK_THRESHOLD) {
            publishLowStockEvent(updatedFlower);
        }

        return toResponse(updatedFlower);
    }

    @Override
    public FlowerResponse update(Long id, UpdateFlowerRequest request) {
        Flower existingFlower = flowerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flower", id));

        Integer oldStock = existingFlower.getStockQuantity();

        existingFlower.setName(request.name());
        existingFlower.setColor(request.color());
        existingFlower.setPrice(request.price());
        existingFlower.setStockQuantity(request.stockQuantity());
        existingFlower.setDescription(request.description());

        Flower updatedFlower = flowerRepository.save(existingFlower);

        if (updatedFlower.getStockQuantity() <= LOW_STOCK_THRESHOLD && oldStock > LOW_STOCK_THRESHOLD) {
            publishLowStockEvent(updatedFlower);
        }

        return toResponse(updatedFlower);
    }

    @Override
    public void saveFlower(Flower flower) {
        Flower savedFlower = flowerRepository.save(flower);
        checkLowStock(savedFlower);
    }

    @Override
    public void delete(Long id) {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flower", id));

        boolean isUsedInBouquets = !flower.getBouquetFlowers().isEmpty();

        if (isUsedInBouquets) {
            throw new IllegalStateException("Cannot delete flower with id " + id +
                    " because it is used in one or more bouquets");
        }

        flowerRepository.delete(flower);
    }

    private void checkLowStock(Flower flower) {
        if (flower.getStockQuantity() <= LOW_STOCK_THRESHOLD) {
            publishLowStockEvent(flower);
        }
    }

    private void publishLowStockEvent(Flower flower) {
        FlowerStockLowEvent event = new FlowerStockLowEvent(
                flower.getId(),
                flower.getName(),
                flower.getColor(),
                flower.getStockQuantity(),
                LOW_STOCK_THRESHOLD,
                LocalDateTime.now()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY_FLOWER_STOCK_LOW,
                event
        );
    }

    private FlowerResponse toResponse(Flower flower) {
        return new FlowerResponse(
                flower.getId(),
                flower.getName(),
                flower.getColor(),
                flower.getPrice(),
                flower.getStockQuantity(),
                flower.getDescription(),
                flower.getCreatedAt()
        );
    }
}

