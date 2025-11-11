package com.escrims.infra.persistence.inmemory;

import com.escrims.model.domain.model.Scrim;
import com.escrims.model.repository.ScrimRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryScrimRepository implements ScrimRepository {
    private final Map<UUID, Scrim> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Scrim> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Scrim> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void save(Scrim scrim) {
        store.put(scrim.getId(), scrim);
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }
}
