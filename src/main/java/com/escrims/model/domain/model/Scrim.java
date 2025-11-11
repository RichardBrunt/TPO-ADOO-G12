package com.escrims.model.domain.model;

import com.escrims.model.domain.state.ScrimState;
import com.escrims.model.domain.state.BuscandoState;
import com.escrims.model.domain.strategy.SelectionStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Scrim {
    private Long id;
    private String titulo;
    private String descripcion;
    private Juego juego;
    private Formato formato;
    private Region region;
    private int mmrMinimo;
    private int mmrMaximo;
    private LocalDateTime fechaHora;
    private Usuario creador;
    private ScrimState estado;
    private SelectionStrategy estrategiaSeleccion;
    private List<Postulacion> postulaciones;
    private List<Confirmacion> confirmaciones;
    private List<Usuario> participantes;

    public Scrim(String titulo, String descripcion, Juego juego, Formato formato, 
                 Region region, int mmrMinimo, int mmrMaximo, LocalDateTime fechaHora, 
                 Usuario creador) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.juego = juego;
        this.formato = formato;
        this.region = region;
        this.mmrMinimo = mmrMinimo;
        this.mmrMaximo = mmrMaximo;
        this.fechaHora = fechaHora;
        this.creador = creador;
        this.estado = new BuscandoState();
        this.postulaciones = new ArrayList<>();
        this.confirmaciones = new ArrayList<>();
        this.participantes = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public Formato getFormato() {
        return formato;
    }

    public void setFormato(Formato formato) {
        this.formato = formato;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public int getMmrMinimo() {
        return mmrMinimo;
    }

    public void setMmrMinimo(int mmrMinimo) {
        this.mmrMinimo = mmrMinimo;
    }

    public int getMmrMaximo() {
        return mmrMaximo;
    }

    public void setMmrMaximo(int mmrMaximo) {
        this.mmrMaximo = mmrMaximo;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    public ScrimState getEstado() {
        return estado;
    }

    public void setEstado(ScrimState estado) {
        this.estado = estado;
    }

    public SelectionStrategy getEstrategiaSeleccion() {
        return estrategiaSeleccion;
    }

    public void setEstrategiaSeleccion(SelectionStrategy estrategiaSeleccion) {
        this.estrategiaSeleccion = estrategiaSeleccion;
    }

    public List<Postulacion> getPostulaciones() {
        return new ArrayList<>(postulaciones);
    }

    public void agregarPostulacion(Postulacion postulacion) {
        this.postulaciones.add(postulacion);
    }

    public void eliminarPostulacion(Postulacion postulacion) {
        this.postulaciones.remove(postulacion);
    }

    public List<Confirmacion> getConfirmaciones() {
        return new ArrayList<>(confirmaciones);
    }

    public void agregarConfirmacion(Confirmacion confirmacion) {
        this.confirmaciones.add(confirmacion);
    }

    public List<Usuario> getParticipantes() {
        return new ArrayList<>(participantes);
    }

    public void agregarParticipante(Usuario usuario) {
        this.participantes.add(usuario);
    }

    public void eliminarParticipante(Usuario usuario) {
        this.participantes.remove(usuario);
    }

    public int getLugaresDisponibles() {
        int totalLugares = formato.getJugadoresPorEquipo() * 2;
        return totalLugares - participantes.size();
    }

    public void cambiarEstado(ScrimState nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void ejecutarAccionEstado() {
        estado.ejecutarAccion(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scrim scrim = (Scrim) o;
        return Objects.equals(id, scrim.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Scrim{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", juego=" + juego +
                ", formato=" + formato +
                ", estado=" + estado.getNombre() +
                '}';
    }
}
