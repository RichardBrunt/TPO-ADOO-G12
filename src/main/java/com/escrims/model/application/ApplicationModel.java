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

    public Scrim crearScrim(ScrimFormData formData) {
        Scrim scrim = scrimService.crearScrim(
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
        
        // Asignar la estrategia de selección al scrim
        if (scrim != null && formData.getEstrategiaSeleccion() != null) {
            scrim.setEstrategiaSeleccion(formData.getEstrategiaSeleccion());
            System.out.println("[MODEL] Estrategia asignada al scrim: " + formData.getEstrategiaSeleccion().getNombre());
        }
        
        // Agregar automáticamente al creador como participante del scrim
        if (scrim != null && usuarioActual != null) {
            scrim.agregarParticipante(usuarioActual);
            System.out.println("[AUTO-JOIN] Usuario '" + usuarioActual.getUsername() + "' agregado automáticamente al scrim " + scrim.getId());
        }
        
        notifyListeners();
        return scrim;
    }

    public void postularseAScrim(Scrim scrim, Rol rol, String mensaje) {
        scrimService.postularseAScrim(scrim, usuarioActual, rol, mensaje);
        notifyListeners();
    }

    // Métodos requeridos por ScrimController
    public boolean login(String username, String password) {
        // Implementación simplificada - en producción validar contra repositorio
        Usuario usuario = new Usuario(username, password, username + "@example.com");
        // Agregar estadísticas básicas
        usuario.agregarEstadistica(new Estadistica(Juego.LOL, 1500, 0, 0.0, 50));
        setUsuarioActual(usuario);
        return true;
    }

    public void logout() {
        setUsuarioActual(null);
    }

    public void crearUsuario(String username, String email, String password, 
                            Region region, int mmr, int latencia, java.util.Set<Rol> roles) {
        Usuario nuevoUsuario = new Usuario(username, password, email);
        // Agregar estadística con los datos proporcionados
        nuevoUsuario.agregarEstadistica(new Estadistica(Juego.LOL, mmr, 0, 0.0, latencia));
        setUsuarioActual(nuevoUsuario);
    }

    public List<Scrim> buscarScrimsDisponibles() {
        return scrimService.listarScrimsDisponibles();
    }

    public List<Scrim> obtenerTodosScrims() {
        return scrimService.listarTodosScrims();
    }

    public Postulacion postularseAScrim(java.util.UUID scrimId, Rol rolDeseado) {
        // Buscar scrim por ID - necesitamos convertir UUID a Long o buscar de otra forma
        List<Scrim> scrims = scrimService.listarTodosScrims();
        Scrim scrim = scrims.stream()
            .filter(s -> s.getId() != null && s.getId().toString().equals(scrimId.toString()))
            .findFirst()
            .orElse(null);
            
        if (scrim != null) {
            Postulacion postulacion = scrimService.postularseAScrim(scrim, usuarioActual, rolDeseado, "");
            notifyListeners();
            return postulacion;
        }
        return null;
    }

    public List<Postulacion> obtenerPostulacionesDeScrim(java.util.UUID scrimId) {
        List<Scrim> scrims = scrimService.listarTodosScrims();
        Scrim scrim = scrims.stream()
            .filter(s -> s.getId() != null && s.getId().toString().equals(scrimId.toString()))
            .findFirst()
            .orElse(null);
            
        if (scrim != null) {
            return new ArrayList<>(scrim.getPostulaciones());
        }
        return new ArrayList<>();
    }

    public List<Postulacion> obtenerMisPostulaciones() {
        if (usuarioActual == null) {
            return new ArrayList<>();
        }
        // Buscar en todos los scrims las postulaciones del usuario actual
        List<Postulacion> misPostulaciones = new ArrayList<>();
        for (Scrim scrim : scrimService.listarTodosScrims()) {
            for (Postulacion p : scrim.getPostulaciones()) {
                if (p.getUsuario().equals(usuarioActual)) {
                    misPostulaciones.add(p);
                }
            }
        }
        return misPostulaciones;
    }

    public void aceptarPostulacion(java.util.UUID postulacionId) {
        // Buscar la postulación en todos los scrims
        for (Scrim scrim : scrimService.listarTodosScrims()) {
            for (Postulacion p : scrim.getPostulaciones()) {
                if (p.getId() != null && p.getId().toString().equals(postulacionId.toString())) {
                    scrimService.aceptarPostulacion(p);
                    notifyListeners();
                    return;
                }
            }
        }
    }

    public void rechazarPostulacion(java.util.UUID postulacionId) {
        // Buscar la postulación en todos los scrims y eliminarla
        for (Scrim scrim : scrimService.listarTodosScrims()) {
            for (Postulacion p : scrim.getPostulaciones()) {
                if (p.getId() != null && p.getId().toString().equals(postulacionId.toString())) {
                    scrim.getPostulaciones().remove(p);
                    notifyListeners();
                    return;
                }
            }
        }
    }

    public java.util.Map<String, Integer> obtenerEstadisticas() {
        java.util.Map<String, Integer> stats = new java.util.HashMap<>();
        stats.put("totalScrims", scrimService.listarTodosScrims().size());
        stats.put("scrimsDisponibles", scrimService.listarScrimsDisponibles().size());
        return stats;
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
        private com.escrims.model.domain.strategy.SelectionStrategy estrategiaSeleccion;

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
        
        public com.escrims.model.domain.strategy.SelectionStrategy getEstrategiaSeleccion() { return estrategiaSeleccion; }
        public void setEstrategiaSeleccion(com.escrims.model.domain.strategy.SelectionStrategy estrategiaSeleccion) { 
            this.estrategiaSeleccion = estrategiaSeleccion; 
        }
    }
}
