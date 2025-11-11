package com.escrims.infra.persistence.inmemory;

import com.escrims.model.domain.model.Postulacion;
import com.escrims.model.repository.PostulacionRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPostulacionRepository implements PostulacionRepository {
    private final Map<UUID, Postulacion> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Postulacion> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Postulacion> findByScrimId(UUID scrimId) {
        List<Postulacion> out = new ArrayList<>();
        for (Postulacion p : store.values()) {
            if (p.getScrimId().equals(scrimId)) out.add(p);
        }
        return out;
    }

    @Override
    public List<Postulacion> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void save(Postulacion postulacion) {
        store.put(postulacion.getId(), postulacion);
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }
}
