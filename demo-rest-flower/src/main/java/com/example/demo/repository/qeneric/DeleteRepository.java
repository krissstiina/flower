package com.example.demo.repository.qeneric;

public interface DeleteRepository<T, ID> {
    void delete(T entity);
    void deleteById(ID id);
}
