package com.example.demo.service;

import com.example.demo.Dto.FlowerRequest;
import com.example.demo.Dto.FlowerResponse;
import com.example.demo.Dto.PagedResponse;
import com.example.demo.Dto.UpdateFlowerRequest;
import com.example.demo.model.Flower;
import org.springframework.stereotype.Service;

import java.util.List;


public interface FlowerService {
    FlowerResponse findById(Long id);
    List<FlowerResponse> findAll();
//    PagedResponse<FlowerResponse> findAllFlowers(int page, int size);
    FlowerResponse create(FlowerRequest request);
    FlowerResponse update(Long id, UpdateFlowerRequest request);
    void delete(Long id);
    Flower findEntityById(Long id);
    void saveFlower(Flower flower);
    FlowerResponse updateStockQuantity(Long id, Integer stockQuantity);

}
