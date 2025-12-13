package com.example.demo.repository;

import com.example.demo.model.Flower;
import com.example.demo.repository.qeneric.CreateRepository;
import com.example.demo.repository.qeneric.DeleteRepository;
import com.example.demo.repository.qeneric.ReadRepository;
import com.example.demo.repository.qeneric.UpdateRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowerRepository extends
        ReadRepository<Flower, Long>,
        CreateRepository<Flower>,
        DeleteRepository<Flower, Long>,
        UpdateRepository<Flower> {
}
