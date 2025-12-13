package com.example.demo.service;

import com.example.demo.Dto.OrderRequest;
import com.example.demo.Dto.OrderResponse;
import com.example.demo.Dto.PagedResponse;
import com.example.demo.Dto.UpdateOrderStatusRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    List<OrderResponse> findAll();
    OrderResponse findById(Long id);
    PagedResponse<OrderResponse> findAllOrders(int page, int size);
    List<OrderResponse> findByStatus(String status);
    OrderResponse create(OrderRequest request);
    OrderResponse updateStatus(Long id, UpdateOrderStatusRequest request);
    OrderResponse updateDeliveryDate(Long id, LocalDateTime deliveryDate);
    OrderResponse cancel(Long id);
    void delete(Long id);
}

