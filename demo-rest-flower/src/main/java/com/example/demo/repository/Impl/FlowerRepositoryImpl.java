package com.example.demo.repository.Impl;

import com.example.demo.model.Flower;
import com.example.demo.repository.FlowerRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class FlowerRepositoryImpl extends BaseRepositoryImpl<Flower, Long>
        implements FlowerRepository {

    public FlowerRepositoryImpl() {
        super(Flower.class);
    }
}


