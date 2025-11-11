package com.escrims.infra.persistence.inmemory;

import com.escrims.model.domain.model.Postulacion;
import com.escrims.model.domain.model.Scrim;
import com.escrims.model.domain.model.Usuario;
import com.escrims.model.repository.PostulacionRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryPostulacionRepository implements PostulacionRepository {
    private final Map<Long, Postulacion> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Postulacion guardar(Postulacion postulacion) {
        if (postulacion.getId() == null) {
            // Asignar ID si es nueva postulación
            Long newId = idGenerator.getAndIncrement();
            postulacion.setId(newId);
        }
        store.put(postulacion.getId(), postulacion);
        return postulacion;
    }

    @Override
    public Optional<Postulacion> buscarPorId(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Postulacion> buscarTodas() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Postulacion> buscarPorScrim(Scrim scrim) {
        List<Postulacion> out = new ArrayList<>();
        for (Postulacion p : store.values()) {
            if (p.getScrim().equals(scrim)) {
                out.add(p);
            }
        }
        return out;
    }

    @Override
    public List<Postulacion> buscarPorUsuario(Usuario usuario) {
        List<Postulacion> out = new ArrayList<>();
        for (Postulacion p : store.values()) {
            if (p.getUsuario().equals(usuario)) {
                out.add(p);
            }
        }
        return out;
    }

    @Override
    public void eliminar(Long id) {
        store.remove(id);
    }
}
