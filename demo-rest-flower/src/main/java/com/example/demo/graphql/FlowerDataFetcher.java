package com.example.demo.graphql;

import com.example.demo.Dto.*;
import com.example.demo.service.FlowerService;
import com.example.demo.service.impl.FlowerServiceImpl;
import com.netflix.graphql.dgs.*;
import graphql.schema.DataFetchingEnvironment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@DgsComponent
public class FlowerDataFetcher {
    private final FlowerService flowerService;

    public FlowerDataFetcher(FlowerService flowerService) {
        this.flowerService = flowerService;
    }

    @DgsQuery
    public List<FlowerResponse> getAllFlowers(){
        return flowerService.findAll();
    }

    @DgsQuery
    public FlowerResponse flowerById(@InputArgument Long id){
        return flowerService.findById(id);
    }

//    @DgsQuery
//    public PagedResponse<FlowerResponse> flowers(
//            @InputArgument int page,
//            @InputArgument int size) {
//        return flowerService.findAllFlowers(page, size);
//    }

    @DgsMutation
    public FlowerResponse createFlower(@InputArgument("input") Map<String, Object> input){
        FlowerRequest request = new FlowerRequest(
                (String) input.get("name"),
                (String) input.get("color"),
                new BigDecimal(input.get("price").toString()),
                Integer.parseInt(input.get("stockQuantity").toString()),
                (String) input.get("description")
        );
        return flowerService.create(request);
    }

    @DgsMutation
    public FlowerResponse updateFlower(@InputArgument Long id, @InputArgument("input") Map<String, Object> input) {
        UpdateFlowerRequest request = new UpdateFlowerRequest(
                (String) input.get("name"),
                (String) input.get("color"),
                new BigDecimal(input.get("price").toString()),
                Integer.parseInt(input.get("stockQuantity").toString()),
                (String) input.get("description")
        );
        return flowerService.update(id, request);
    }

    @DgsMutation
    public Long deleteFlower(@InputArgument Long id) {
        flowerService.delete(id);
        return id;
    }

    @DgsData(parentType = "Flower", field = "createdAt")
    public String getCreatedAt(DataFetchingEnvironment dfe) {
        FlowerResponse flower = dfe.getSource();
        return flower.getCreatedAt() != null ? flower.getCreatedAt().toString() : null;
    }
}