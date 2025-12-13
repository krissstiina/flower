package com.example.demo.controllers;

import com.example.demo.Dto.*;
import com.example.demo.assemblers.OrderModelAssembler;
import com.example.demo.endpoints.OrderApi;
import com.example.demo.service.impl.OrderServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderController implements OrderApi {

    private final OrderServiceImpl orderService;
    private final OrderModelAssembler orderModelAssembler;
    private final PagedResourcesAssembler<OrderResponse> pagedResourcesAssembler;

    public OrderController(OrderServiceImpl orderService, OrderModelAssembler orderModelAssembler, PagedResourcesAssembler<OrderResponse> pagedResourcesAssembler) {
        this.orderService = orderService;
        this.orderModelAssembler = orderModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    public CollectionModel<EntityModel<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.findAll();
        return orderModelAssembler.toCollectionModel(orders);
    }

    @Override
    public EntityModel<OrderResponse> getOrderById(Long id) {
        OrderResponse order = orderService.findById(id);
        return orderModelAssembler.toModel(order);
    }

    @Override
    public PagedModel<EntityModel<OrderResponse>> getAllOrders(int page, int size) {
        PagedResponse<OrderResponse> pagedResponse = orderService.findAllOrders(page, size);

        Page<OrderResponse> orderPage = new PageImpl<>(
                pagedResponse.content(),
                PageRequest.of(pagedResponse.pageNumber(),pagedResponse.pageSize()),
                pagedResponse.totalElements()
        );
        return pagedResourcesAssembler.toModel(orderPage, orderModelAssembler);
    }

    @Override
    public CollectionModel<EntityModel<OrderResponse>> getOrdersByStatus(String status) {
        List<EntityModel<OrderResponse>> orders = orderService.findByStatus(status).stream()
                .map(orderModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).getOrdersByStatus(status)).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAllOrders()).withRel("all-orders")
        );
    }

    @Override
    public ResponseEntity<EntityModel<OrderResponse>> createOrder(OrderRequest request) {
        OrderResponse createdOrder = orderService.create(request);
        EntityModel<OrderResponse> entityModel = orderModelAssembler.toModel(createdOrder);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
     }

    @Override
    public EntityModel<OrderResponse> updateOrderStatus(Long id, UpdateOrderStatusRequest request) {
        OrderResponse updateOrder = orderService.updateStatus(id,request);
        return orderModelAssembler.toModel(updateOrder);
    }

    @Override
    public EntityModel<OrderResponse> updateDeliveryDate(Long id, LocalDateTime deliveryDate) {
        OrderResponse updateOrder = orderService.updateDeliveryDate(id,deliveryDate);
        return orderModelAssembler.toModel(updateOrder);
    }

    @Override
    public EntityModel<OrderResponse> cancelOrder(Long id) {
        OrderResponse cancelledOrder = orderService.cancel(id);
        return orderModelAssembler.toModel(cancelledOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        orderService.delete(id);
    }
}