package com.escrims.infra.persistence.inmemory;

import com.escrims.model.domain.model.Usuario;
import com.escrims.model.repository.UsuarioRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUsuarioRepository implements UsuarioRepository {
    private final Map<UUID, Usuario> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Usuario> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Usuario> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void save(Usuario usuario) {
        store.put(usuario.getId(), usuario);
    }

    @Override
    public void deleteById(UUID id) {
        store.remove(id);
    }
}
