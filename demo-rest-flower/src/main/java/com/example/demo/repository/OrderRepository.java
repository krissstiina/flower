package com.example.demo.repository;

import com.example.demo.model.Order;
import com.example.demo.repository.qeneric.CreateRepository;
import com.example.demo.repository.qeneric.DeleteRepository;
import com.example.demo.repository.qeneric.ReadRepository;
import com.example.demo.repository.qeneric.UpdateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends ReadRepository<Order,Long>, CreateRepository<Order>, UpdateRepository<Order>, DeleteRepository<Order,Long>{
    Page<Order> findAllByOrderByIdDesc(Pageable pageable);
    List<Order> findByStatus(String status);
}
