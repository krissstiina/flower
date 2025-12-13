package com.example.demo.repository.Impl;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepositoryImpl extends BaseRepositoryImpl<Order, Long>
        implements OrderRepository {

    public OrderRepositoryImpl() {
        super(Order.class);
    }

    @Override
    public Page<Order> findAllByOrderByIdDesc(Pageable pageable) {
        Long total = getEntityManager()
                .createQuery("SELECT COUNT(o) FROM Order o", Long.class)
                .getSingleResult();

        List<Order> content = getEntityManager()
                .createQuery("SELECT o FROM Order o ORDER BY o.id DESC", Order.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<Order> findByStatus(String status) {
        return getEntityManager()
                .createQuery("SELECT o FROM Order o WHERE o.status = :status", Order.class)
                .setParameter("status", status)
                .getResultList();
    }
}
