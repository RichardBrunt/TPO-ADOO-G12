package com.escrims.infra.persistence.inmemory;

import com.escrims.model.domain.model.*;
import com.escrims.model.repository.ScrimRepository;
import com.escrims.model.domain.state.BuscandoState;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryScrimRepository implements ScrimRepository {
    private final Map<Long, Scrim> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Scrim guardar(Scrim scrim) {
        if (scrim.getId() == null) {
            scrim.setId(idGenerator.getAndIncrement());
        }
        store.put(scrim.getId(), scrim);
        return scrim;
    }

    @Override
    public Optional<Scrim> buscarPorId(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Scrim> buscarTodos() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Scrim> buscarPorJuego(Juego juego) {
        return store.values().stream()
            .filter(s -> s.getJuego() == juego)
            .collect(Collectors.toList());
    }

    @Override
    public List<Scrim> buscarPorRegion(Region region) {
        return store.values().stream()
            .filter(s -> s.getRegion() == region)
            .collect(Collectors.toList());
    }

    @Override
    public List<Scrim> buscarPorCreador(Usuario creador) {
        return store.values().stream()
            .filter(s -> s.getCreador().equals(creador))
            .collect(Collectors.toList());
    }

    @Override
    public List<Scrim> buscarDisponibles() {
        return store.values().stream()
            .filter(s -> s.getEstado() instanceof BuscandoState)
            .collect(Collectors.toList());
    }

    @Override
    public void eliminar(Long id) {
        store.remove(id);
    }
}
