package com.example.demo.repository;

import com.example.demo.model.Bouquet;
import com.example.demo.repository.qeneric.CreateRepository;
import com.example.demo.repository.qeneric.DeleteRepository;
import com.example.demo.repository.qeneric.ReadRepository;
import com.example.demo.repository.qeneric.UpdateRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BouquetRepository extends ReadRepository<Bouquet, Long>, CreateRepository<Bouquet>, DeleteRepository<Bouquet, Long>, UpdateRepository<Bouquet> {
}
