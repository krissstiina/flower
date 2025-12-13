package com.example.demo.repository.qeneric;

import java.util.List;
import java.util.Optional;

public interface ReadRepository<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
}
