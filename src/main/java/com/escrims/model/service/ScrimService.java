package com.escrims.model.service;

import com.escrims.model.domain.builder.ScrimBuilder;
import com.escrims.model.domain.events.DomainEventBus;
import com.escrims.model.domain.events.ScrimStateChanged;
import com.escrims.model.domain.model.*;
import com.escrims.model.domain.state.*;
import com.escrims.model.repository.PostulacionRepository;
import com.escrims.model.repository.ScrimRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ScrimService {
    private final ScrimRepository scrimRepository;
    private final PostulacionRepository postulacionRepository;
    private final DomainEventBus eventBus;

    public ScrimService(ScrimRepository scrimRepository, 
                       PostulacionRepository postulacionRepository,
                       DomainEventBus eventBus) {
        this.scrimRepository = scrimRepository;
        this.postulacionRepository = postulacionRepository;
        this.eventBus = eventBus;
    }

    public Scrim crearScrim(String titulo, String descripcion, Juego juego,
                           Formato formato, Region region, int mmrMinimo,
                           int mmrMaximo, LocalDateTime fechaHora, Usuario creador) {
        Scrim scrim = new ScrimBuilder()
                .titulo(titulo)
                .descripcion(descripcion)
                .juego(juego)
                .formato(formato)
                .region(region)
                .mmrMinimo(mmrMinimo)
                .mmrMaximo(mmrMaximo)
                .fechaHora(fechaHora)
                .creador(creador)
                .build();

        return scrimRepository.guardar(scrim);
    }

    public Optional<Scrim> buscarScrimPorId(Long id) {
        return scrimRepository.buscarPorId(id);
    }

    public List<Scrim> listarTodosScrims() {
        return scrimRepository.buscarTodos();
    }

    public List<Scrim> listarScrimsDisponibles() {
        return scrimRepository.buscarDisponibles();
    }

    public List<Scrim> listarScrimsPorJuego(Juego juego) {
        return scrimRepository.buscarPorJuego(juego);
    }

    public Postulacion postularseAScrim(Scrim scrim, Usuario usuario, Rol rol, String mensaje) {
        Postulacion postulacion = new Postulacion(usuario, scrim, rol, mensaje);
        scrim.agregarPostulacion(postulacion);
        postulacionRepository.guardar(postulacion);
        scrimRepository.guardar(scrim);
        return postulacion;
    }

    public void aceptarPostulacion(Postulacion postulacion) {
        postulacion.aceptar();
        Scrim scrim = postulacion.getScrim();
        scrim.agregarParticipante(postulacion.getUsuario());
        postulacionRepository.guardar(postulacion);
        scrimRepository.guardar(scrim);
    }

    public void cambiarEstadoScrim(Scrim scrim, ScrimState nuevoEstado) {
        ScrimState estadoAnterior = scrim.getEstado();
        scrim.cambiarEstado(nuevoEstado);
        scrimRepository.guardar(scrim);
        
        ScrimStateChanged evento = new ScrimStateChanged(scrim, estadoAnterior, nuevoEstado);
        eventBus.publicar(evento);
    }

    public void iniciarScrim(Scrim scrim) {
        cambiarEstadoScrim(scrim, new EnCursoState());
    }

    public void finalizarScrim(Scrim scrim) {
        cambiarEstadoScrim(scrim, new FinalizadoState());
    }

    public void armarLobby(Scrim scrim) {
        cambiarEstadoScrim(scrim, new LobbyArmadoState());
    }

    public List<Postulacion> listarPostulacionesDeScrim(Scrim scrim) {
        return postulacionRepository.buscarPorScrim(scrim);
    }

    public void eliminarScrim(Long id) {
        scrimRepository.eliminar(id);
    }
}
