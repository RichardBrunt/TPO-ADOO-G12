package com.escrims.model.repository;

import com.escrims.model.domain.model.*;
import java.util.List;
import java.util.Optional;

public interface ScrimRepository {
    Scrim guardar(Scrim scrim);
    Optional<Scrim> buscarPorId(Long id);
    List<Scrim> buscarTodos();
    List<Scrim> buscarPorJuego(Juego juego);
    List<Scrim> buscarPorRegion(Region region);
    List<Scrim> buscarPorCreador(Usuario creador);
    List<Scrim> buscarDisponibles();
    void eliminar(Long id);
}
