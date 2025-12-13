package com.example.demo.service;

import com.example.demo.Dto.*;
import com.example.demo.model.Bouquet;

import java.util.List;

public interface BouquetService {
    List<BouquetResponse> findAll();
//    PagedResponse<BouquetResponse> findAllBouquets(int page, int size);
    BouquetResponse create(BouquetRequest request);
    BouquetResponse findById(Long id);
    BouquetResponse updateComposition(Long id, List<BouquetFlowerItem> flowers);
    BouquetResponse update(Long id, UpdateBouquetRequest request);
    void delete(Long id);
    BouquetResponse updateStock(Long id, Integer stockQuantity);
    void saveBouquet(Bouquet bouquet);
    Bouquet findEntityById(Long id);
}
