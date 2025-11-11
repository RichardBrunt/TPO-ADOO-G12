package com.escrims.model.application;

import com.escrims.model.domain.model.*;
import com.escrims.model.service.ScrimService;

import java.util.ArrayList;
import java.util.List;

public class ApplicationModel {
    private Usuario usuarioActual;
    private Scrim scrimSeleccionado;
    private final ScrimService scrimService;
    private final List<ModelChangeListener> listeners;

    public ApplicationModel(ScrimService scrimService) {
        this.scrimService = scrimService;
        this.listeners = new ArrayList<>();
    }

    public void addListener(ModelChangeListener listener) {
        listeners.add(listener);
    }

    public void addModelChangeListener(ModelChangeListener listener) {
        addListener(listener);
    }

    public void removeListener(ModelChangeListener listener) {
        listeners.remove(listener);
    }

    public boolean isLoggedIn() {
        return usuarioActual != null;
    }

    private void notifyListeners() {
        for (ModelChangeListener listener : listeners) {
            listener.onModelChanged();
        }
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        notifyListeners();
    }

    public Scrim getScrimSeleccionado() {
        return scrimSeleccionado;
    }

    public void setScrimSeleccionado(Scrim scrim) {
        this.scrimSeleccionado = scrim;
        notifyListeners();
    }

    public ScrimService getScrimService() {
        return scrimService;
    }

    public List<Scrim> obtenerScrimsDisponibles() {
        return scrimService.listarScrimsDisponibles();
    }

    public List<Scrim> obtenerScrimsPorJuego(Juego juego) {
        return scrimService.listarScrimsPorJuego(juego);
    }

    public void crearScrim(ScrimFormData formData) {
        scrimService.crearScrim(
            formData.getTitulo(),
            formData.getDescripcion(),
            formData.getJuego(),
            formData.getFormato(),
            formData.getRegion(),
            formData.getMmrMinimo(),
            formData.getMmrMaximo(),
            formData.getFechaHora(),
            usuarioActual
        );
        notifyListeners();
    }

    public void postularseAScrim(Scrim scrim, Rol rol, String mensaje) {
        scrimService.postularseAScrim(scrim, usuarioActual, rol, mensaje);
        notifyListeners();
    }

    public static class ScrimFormData {
        private String titulo;
        private String descripcion;
        private Juego juego;
        private Formato formato;
        private Region region;
        private int mmrMinimo;
        private int mmrMaximo;
        private java.time.LocalDateTime fechaHora;

        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }

        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

        public Juego getJuego() { return juego; }
        public void setJuego(Juego juego) { this.juego = juego; }

        public Formato getFormato() { return formato; }
        public void setFormato(Formato formato) { this.formato = formato; }

        public Region getRegion() { return region; }
        public void setRegion(Region region) { this.region = region; }

        public int getMmrMinimo() { return mmrMinimo; }
        public void setMmrMinimo(int mmrMinimo) { this.mmrMinimo = mmrMinimo; }

        public int getMmrMaximo() { return mmrMaximo; }
        public void setMmrMaximo(int mmrMaximo) { this.mmrMaximo = mmrMaximo; }

        public java.time.LocalDateTime getFechaHora() { return fechaHora; }
        public void setFechaHora(java.time.LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    }
}
