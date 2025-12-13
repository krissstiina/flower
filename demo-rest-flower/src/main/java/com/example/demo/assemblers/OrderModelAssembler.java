package com.example.demo.assemblers;

import com.example.demo.Dto.OrderResponse;
import com.example.demo.controllers.OrderController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<OrderResponse, EntityModel<OrderResponse>> {

    @Override
    public EntityModel<OrderResponse> toModel(OrderResponse order) {
        EntityModel<OrderResponse> entityModel = EntityModel.of(order,
                linkTo(methodOn(OrderController.class).getOrderById(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getAllOrders()).withRel("orders"),
                linkTo(methodOn(OrderController.class).getAllOrders(0, 10)).withRel("orders-paged")
        );

        if ("PENDING".equals(order.getStatus()) || "PROCESSING".equals(order.getStatus())) {
            entityModel.add(
                    linkTo(methodOn(OrderController.class).cancelOrder(order.getId())).withRel("cancel").withType("POST"),
                    linkTo(methodOn(OrderController.class).updateOrderStatus(order.getId(), null)).withRel("update-status").withType("PUT")
            );
        }

        if ("PENDING".equals(order.getStatus())) {
            entityModel.add(
                    linkTo(methodOn(OrderController.class).updateDeliveryDate(order.getId(), null)).withRel("update-delivery-date").withType("PATCH")
            );
        }

        return entityModel;
    }

    @Override
    public CollectionModel<EntityModel<OrderResponse>> toCollectionModel(Iterable<? extends OrderResponse> entities) {
        CollectionModel<EntityModel<OrderResponse>> collectionModel = RepresentationModelAssembler.super.toCollectionModel(entities);
        collectionModel.add(linkTo(methodOn(OrderController.class).getAllOrders()).withSelfRel());
        return collectionModel;
    }
}
