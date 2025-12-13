package com.example.demo.controllers;

import com.example.demo.Dto.FlowerRequest;
import com.example.demo.Dto.FlowerResponse;
import com.example.demo.Dto.UpdateFlowerRequest;
import com.example.demo.Dto.UpdateStockRequest;
import com.example.demo.assemblers.FlowerModelAssembler;
import com.example.demo.endpoints.FlowerApi;
import com.example.demo.service.FlowerService;
import jakarta.validation.Valid;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FlowerController implements FlowerApi {

    private final FlowerService flowerService;
    private final FlowerModelAssembler flowerModelAssembler;
    private final PagedResourcesAssembler<FlowerResponse> pagedResourcesAssembler;

    public FlowerController(FlowerService flowerService, FlowerModelAssembler flowerModelAssembler, PagedResourcesAssembler<FlowerResponse> pagedResourcesAssembler ) {
        this.flowerService = flowerService;
        this.flowerModelAssembler = flowerModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    public CollectionModel<EntityModel<FlowerResponse>> getAllFlowers() {
        List<FlowerResponse> flowers = flowerService.findAll();
        return flowerModelAssembler.toCollectionModel(flowers);
    }

    @Override
    public EntityModel<FlowerResponse> getFlowerById(Long id){
        FlowerResponse flower = flowerService.findById(id);
        return flowerModelAssembler.toModel(flower);
    }

    @Override
    public EntityModel<FlowerResponse> updateFlowerStock(@PathVariable Long id, @Valid @RequestBody UpdateStockRequest request) {
        FlowerResponse updatedFlower = flowerService.updateStockQuantity(id, request.stockQuantity());
        return flowerModelAssembler.toModel(updatedFlower);
    }

    @Override
    public ResponseEntity<EntityModel<FlowerResponse>> createFlower(FlowerRequest request){
        FlowerResponse createdFlower = flowerService.create(request);
        EntityModel<FlowerResponse> entityModel = flowerModelAssembler.toModel(createdFlower);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @Override
    public EntityModel<FlowerResponse> updateFlower(Long id, UpdateFlowerRequest request) {
        FlowerResponse updatedFlower = flowerService.update(id, request);
        return flowerModelAssembler.toModel(updatedFlower);
    }

    @Override
    public void deleteFlower(Long id) {
        flowerService.delete(id);
    }
}