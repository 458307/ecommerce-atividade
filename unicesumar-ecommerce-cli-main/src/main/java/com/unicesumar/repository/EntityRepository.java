package com.unicesumar.repository;

import java.util.List;
import java.util.Optional;


public interface EntityRepository<T> {
    void save(T entity);
    Optional<T> findById(int id);
    List<T> findAll();
    void deleteById(int id);
}
