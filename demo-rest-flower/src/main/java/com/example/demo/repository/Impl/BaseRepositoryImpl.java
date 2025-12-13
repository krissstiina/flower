package com.example.demo.repository.Impl;


import java.util.List;
import java.util.Optional;

import com.example.demo.repository.qeneric.CreateRepository;
import com.example.demo.repository.qeneric.DeleteRepository;
import com.example.demo.repository.qeneric.ReadRepository;
import com.example.demo.repository.qeneric.UpdateRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

public class BaseRepositoryImpl<T, ID>
        implements ReadRepository<T, ID>,
        CreateRepository<T>,
        UpdateRepository<T>,
        DeleteRepository<T, ID> {

    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<T> entityClass;

    public BaseRepositoryImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Transactional
    @Override
    public T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Transactional
    @Override
    public T create(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Transactional
    @Override
    public void update(T entity) {
        entityManager.merge(entity);
    }

    @Transactional
    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    @Transactional
    @Override
    public List<T> findAll() {
        return entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList();
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected Class<T> getEntityClass() {
        return entityClass;
    }

    @Transactional
    @Override
    public void delete(T entity) {
        if (entity != null && entityManager.contains(entity)) {
            entityManager.remove(entity);
        } else {
            entityManager.remove(entityManager.merge(entity));
        }
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        Optional<T> entity = findById(id);
        entity.ifPresent(this::delete);
    }
}