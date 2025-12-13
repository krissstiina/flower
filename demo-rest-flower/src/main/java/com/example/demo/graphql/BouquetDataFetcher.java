package com.example.demo.graphql;

import com.example.demo.Dto.*;
import com.example.demo.service.BouquetService;
import com.netflix.graphql.dgs.*;

import java.util.List;
import java.util.Map;

@DgsComponent
public class BouquetDataFetcher {
    private final BouquetService bouquetService;

    public BouquetDataFetcher(BouquetService bouquetService) {
        this.bouquetService = bouquetService;
    }

    @DgsQuery
    public List<BouquetResponse> getAllBouquets(){
        return bouquetService.findAll();
    }

    @DgsQuery
    public BouquetResponse bouquetById(@InputArgument Long id){
        return bouquetService.findById(id);
    }

//    @DgsQuery
//    public PagedResponse<BouquetResponse> bouquets(
//            @InputArgument int page,
//            @InputArgument int size) {
//        return bouquetService.findAllBouquets(page, size);
//    }

    @DgsMutation
    public BouquetResponse createBouquet(@InputArgument("input")Map<String, Object> input){
        @SuppressWarnings("unchecked")
        List<BouquetFlowerItem> flowers = ((List<Map<String, Object>>) input.get("flowers")).stream()
                .map(flower -> new BouquetFlowerItem(
                        Long.parseLong(flower.get("flowerId").toString()),
                        Integer.parseInt(flower.get("quantity").toString())
                )).toList();

        return bouquetService.create(new BouquetRequest(
                (String) input.get("name"),
                Integer.parseInt(input.get("stockQuantity").toString()),
                (String) input.get("description"),
                flowers
        ));
    }

    @DgsMutation
    public BouquetResponse updateBouquet(@InputArgument Long id, @InputArgument("input") Map<String,Object> input){
        UpdateBouquetRequest request = new UpdateBouquetRequest(
                (String) input.get("name"),
                Integer.parseInt(input.get("stockQuantity").toString()),
                (String) input.get("description")
        );

        return bouquetService.update(id,request);
    }

    @DgsMutation
    public BouquetResponse updateBouquetComposition(@InputArgument Long id,
                                                    @InputArgument List<Map<String, Object>> flowers) {
        List<BouquetFlowerItem> bouquetFlowers = flowers.stream()
                .map(flower -> new BouquetFlowerItem(
                        Long.parseLong(flower.get("flowerId").toString()),
                        Integer.parseInt(flower.get("quantity").toString())
                ))
                .toList();

        return bouquetService.updateComposition(id, bouquetFlowers);
    }

    @DgsMutation
    public Long deleteBouquet(@InputArgument Long id){
        bouquetService.delete(id);
        return id;
    }
}