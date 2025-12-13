package com.example.demo.repository.Impl;

import com.example.demo.model.Bouquet;
import com.example.demo.repository.BouquetRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BouquetRepositoryImpl extends BaseRepositoryImpl<Bouquet, Long>
        implements BouquetRepository {

    public BouquetRepositoryImpl() {
        super(Bouquet.class);
    }
}
