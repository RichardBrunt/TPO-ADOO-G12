package com.escrims.infra.persistence.inmemory;

import com.escrims.model.domain.model.Usuario;
import com.escrims.model.repository.UsuarioRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUsuarioRepository implements UsuarioRepository {
    private final Map<Long, Usuario> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Usuario guardar(Usuario usuario) {
        if (usuario.getId() == null) {
            Long newId = idGenerator.getAndIncrement();
            usuario.setId(newId);
        }
        store.put(usuario.getId(), usuario);
        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return store.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return store.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<Usuario> buscarTodos() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void eliminar(Long id) {
        store.remove(id);
    }

    @Override
    public boolean existePorUsername(String username) {
        return buscarPorUsername(username).isPresent();
    }

    @Override
    public boolean existePorEmail(String email) {
        return buscarPorEmail(email).isPresent();
    }
}
