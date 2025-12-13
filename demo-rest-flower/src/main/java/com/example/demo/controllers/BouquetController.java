package com.example.demo.controllers;

import com.example.demo.Dto.*;
import com.example.demo.assemblers.BouquetModelAssembler;
import com.example.demo.endpoints.BouquetApi;
import com.example.demo.service.BouquetService;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BouquetController implements BouquetApi {

    private final BouquetService bouquetService;
    private final BouquetModelAssembler bouquetModelAssembler;
    private final PagedResourcesAssembler<BouquetResponse> pagedResourcesAssembler;

    public BouquetController(BouquetService bouquetService, BouquetModelAssembler bouquetModelAssembler, PagedResourcesAssembler<BouquetResponse> pagedResourcesAssembler) {
        this.bouquetService = bouquetService;
        this.bouquetModelAssembler = bouquetModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    public CollectionModel<EntityModel<BouquetResponse>> getAllBouquets() {
        List<BouquetResponse> bouquet = bouquetService.findAll();
        return bouquetModelAssembler.toCollectionModel(bouquet);
    }

    @Override
    public EntityModel<BouquetResponse> getBouquetById(Long id) {
        BouquetResponse bouquet = bouquetService.findById(id);
        return bouquetModelAssembler.toModel(bouquet);
    }

//    @Override
//    public PagedModel<EntityModel<BouquetResponse>> getBouquets(int page, int size) {
//        PagedResponse<BouquetResponse> pagedResponse = bouquetService.findAllBouquets(page, size);
//        Page<BouquetResponse> bouquetPage = new PageImpl<>(
//                pagedResponse.content(),
//                PageRequest.of(pagedResponse.pageNumber(), pagedResponse.pageSize()),
//                pagedResponse.totalElements()
//        );
//
//        return pagedResourcesAssembler.toModel(bouquetPage, bouquetModelAssembler);
//    }


    @Override
    public ResponseEntity<EntityModel<BouquetResponse>>createBouquet(BouquetRequest request) {
        BouquetResponse createdBouquet = bouquetService.create(request);
        EntityModel<BouquetResponse> entityModel = bouquetModelAssembler.toModel(createdBouquet);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @Override
    public EntityModel<BouquetResponse> updateBouquet(Long id, UpdateBouquetRequest request) {
        BouquetResponse updatedBouquet = bouquetService.update(id, request);
        return bouquetModelAssembler.toModel(updatedBouquet);
    }

    @Override
    public EntityModel<BouquetResponse> updateBouquetComposition(Long id, List<BouquetFlowerItem> flowers) {
        BouquetResponse updatedBouquet = bouquetService.updateComposition(id, flowers);
        return bouquetModelAssembler.toModel(updatedBouquet);
    }

    @Override
    public void deleteBouquet(Long id) {
        bouquetService.delete(id);
    }
}

