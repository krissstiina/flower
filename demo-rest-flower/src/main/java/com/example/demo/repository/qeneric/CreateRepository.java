package com.example.demo.repository.qeneric;

public interface CreateRepository<T> {
    T save(T entity);
    T create(T entity);
}
