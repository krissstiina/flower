package com.example.demo.repository.qeneric;

public interface UpdateRepository<T> {
    void update(T entity);
}
