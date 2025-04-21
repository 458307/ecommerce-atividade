package com.unicesumar.repository;

import com.unicesumar.entities.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryRepository<T extends Entity> implements EntityRepository<T> {
    private ArrayList<T> dataStore = new ArrayList<>();

    @Override
    public void save(T entity) {
        this.dataStore.removeIf(e -> e.getId() == entity.getId());
        this.dataStore.add(entity);
    }

    @Override
    public Optional<T> findById(int id) {
        return this.dataStore.stream().filter(e -> e.getId() == id).findFirst();
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(this.dataStore);
    }

    @Override
    public void deleteById(int id) {
        this.dataStore.removeIf(e -> e.getId() == id);
    }
} 
