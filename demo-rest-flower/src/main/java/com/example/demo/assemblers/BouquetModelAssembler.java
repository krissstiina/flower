package com.example.demo.assemblers;

import com.example.demo.Dto.BouquetResponse;
import com.example.demo.Dto.FlowerResponse;
import com.example.demo.controllers.BouquetController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BouquetModelAssembler implements RepresentationModelAssembler<BouquetResponse, EntityModel<BouquetResponse>> {

    @Override
    public EntityModel<BouquetResponse> toModel(BouquetResponse bouquet) {
        return EntityModel.of(bouquet,
                linkTo(methodOn(BouquetController.class).getBouquetById(bouquet.getId())).withSelfRel(),
                linkTo(methodOn(BouquetController.class).getAllBouquets()).withRel("bouquets"),
//                linkTo(methodOn(BouquetController.class).getBouquets(0, 10)).withRel("bouquets-paged"),
                linkTo(methodOn(BouquetController.class).updateBouquetComposition(bouquet.getId(), null)).withRel("update-composition").withType("PATCH"),
                linkTo(methodOn(BouquetController.class).getAllBouquets()).withRel("collection")
        );
    }

    @Override
    public CollectionModel<EntityModel<BouquetResponse>> toCollectionModel(Iterable<? extends BouquetResponse> entities){
        CollectionModel<EntityModel<BouquetResponse>> collectionModel = RepresentationModelAssembler.super.toCollectionModel(entities);
        collectionModel.add(linkTo(methodOn(BouquetController.class).getAllBouquets()).withSelfRel());
        return collectionModel;

    }
}
