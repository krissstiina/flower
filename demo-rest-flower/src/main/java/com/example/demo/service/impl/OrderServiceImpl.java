package com.example.demo.service.impl;

import com.example.demo.Dto.*;
import com.example.demo.config.RabbitMQConfig;
import com.example.demo.exseption.InsufficientStockException;
import com.example.demo.exseption.InvalidOrderStatusException;
import com.example.demo.exseption.ResourceNotFoundException;
import com.example.demo.model.Bouquet;
import com.example.demo.model.BouquetFlower;
import com.example.demo.model.Flower;
import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.BouquetService;
import com.example.demo.service.OrderService;
import org.example.events.OrderDeliveredEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final BouquetService bouquetService;
    private final RabbitTemplate rabbitTemplate;

    public OrderServiceImpl(OrderRepository orderRepository, BouquetService bouquetService, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.bouquetService = bouquetService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
        return toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<OrderResponse> findAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findAllByOrderByIdDesc(pageable);

        List<OrderResponse> content = orderPage.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.isLast()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findByStatus(String status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse create(OrderRequest request) {
        Bouquet bouquet = bouquetService.findEntityById(request.bouquetId());

        if (bouquet.getStockQuantity() < request.quantity()) {
            throw new InsufficientStockException("Bouquet", bouquet.getId(), bouquet.getStockQuantity(), request.quantity());
        }

        bouquet.setStockQuantity(bouquet.getStockQuantity() - request.quantity());
        bouquetService.saveBouquet(bouquet);

        BigDecimal totalPrice = bouquet.getPrice().multiply(BigDecimal.valueOf(request.quantity()));

        Order order = new Order();
        order.setBouquet(bouquet);
        order.setQuantity(request.quantity());
        order.setPrice(totalPrice);
        order.setStatus("PENDING");
        order.setAddress(request.deliveryAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setDeliveryDate(null);

        Order savedOrder = orderRepository.save(order);
        return toResponse(savedOrder);
    }

    @Override
    public OrderResponse updateStatus(Long id, UpdateOrderStatusRequest request) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));

        String oldStatus = existingOrder.getStatus();

        validateStatusTransition(oldStatus, request.status());

        if ("DELIVERED".equals(request.status())) {
            existingOrder.setDeliveryDate(LocalDateTime.now());

            publishOrderDeliveredEvent(existingOrder);
        }

        existingOrder.setStatus(request.status());

        Order updatedOrder = orderRepository.save(existingOrder);
        return toResponse(updatedOrder);
    }

    private void publishOrderDeliveredEvent(Order order) {
        Bouquet bouquet = order.getBouquet();

        OrderDeliveredEvent event = new OrderDeliveredEvent(
                order.getId(),
                bouquet.getId(),
                bouquet.getName(),
                "Клиент",
                order.getAddress(),
                order.getQuantity(),
                order.getPrice(),
                order.getOrderDate(),
                order.getDeliveryDate()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY_ORDER_DELIVERED,
                event
        );
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {
        if ("CANCELLED".equals(currentStatus) && !"CANCELLED".equals(newStatus)) {
            throw new InvalidOrderStatusException(currentStatus, newStatus);
        }
        if ("DELIVERED".equals(currentStatus) && !"DELIVERED".equals(newStatus)) {
            throw new InvalidOrderStatusException(currentStatus, newStatus);
        }
    }

    @Override
    public OrderResponse updateDeliveryDate(Long id, LocalDateTime deliveryDate) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));

        existingOrder.setDeliveryDate(deliveryDate);

        Order updatedOrder = orderRepository.save(existingOrder);
        return toResponse(updatedOrder);
    }

    @Override
    public OrderResponse cancel(Long id) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));

        if ("DELIVERED".equals(existingOrder.getStatus()) || "CANCELLED".equals(existingOrder.getStatus())) {
            throw new InvalidOrderStatusException(
                    existingOrder.getStatus(),
                    "CANCELLED"
            );
        }

        Bouquet bouquet = existingOrder.getBouquet();
        bouquet.setStockQuantity(bouquet.getStockQuantity() + existingOrder.getQuantity());
        bouquetService.saveBouquet(bouquet);

        existingOrder.setStatus("CANCELLED");

        Order cancelledOrder = orderRepository.save(existingOrder);
        return toResponse(cancelledOrder);
    }

    @Override
    public void delete(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
        orderRepository.delete(order);
    }

    private OrderResponse toResponse(Order order) {
        Bouquet bouquet = order.getBouquet();

        List<BouquetFlowerResponse> flowerResponses = mapToBouquetFlowerResponse(bouquet.getFlowers());

        BouquetResponse bouquetResponse = new BouquetResponse(
                bouquet.getId(),
                bouquet.getName(),
                bouquet.getPrice(),
                bouquet.getStockQuantity(),
                flowerResponses,
                bouquet.getCreatedAt(),
                bouquet.getUpdatedAt() != null ? bouquet.getUpdatedAt() : bouquet.getCreatedAt()
        );

        return new OrderResponse(
                order.getId(),
                bouquetResponse,
                order.getQuantity(),
                order.getPrice(),
                order.getStatus(),
                order.getAddress(),
                order.getOrderDate(),
                order.getDeliveryDate(),
                order.getCreatedAt()
        );
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
}


