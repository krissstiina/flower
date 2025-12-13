package com.example.demo.assemblers;

import com.example.demo.Dto.FlowerResponse;
import com.example.demo.controllers.FlowerController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FlowerModelAssembler implements RepresentationModelAssembler<FlowerResponse, EntityModel<FlowerResponse>> {

    @Override
    public EntityModel<FlowerResponse> toModel(FlowerResponse flower) {
        return EntityModel.of(flower,
                linkTo(methodOn(FlowerController.class).getFlowerById(flower.getId())).withSelfRel(),
                linkTo(methodOn(FlowerController.class).getAllFlowers()).withRel("flowers"),
                linkTo(methodOn(FlowerController.class).updateFlowerStock(flower.getId(), null)).withRel("update-stock").withType("PATCH")
        );
    }

    @Override
    public CollectionModel<EntityModel<FlowerResponse>> toCollectionModel(Iterable<? extends FlowerResponse> entities) {
        CollectionModel<EntityModel<FlowerResponse>> collectionModel = RepresentationModelAssembler.super.toCollectionModel(entities);
        collectionModel.add(linkTo(methodOn(FlowerController.class).getAllFlowers()).withSelfRel());
        return collectionModel;
    }
}