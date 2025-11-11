package com.escrims.model.application;

import com.escrims.model.domain.model.*;
import com.escrims.model.domain.state.*;
import com.escrims.model.repository.UsuarioRepository;
import com.escrims.model.service.ScrimService;
import com.escrims.infra.notification.NotificationFacade;
import com.escrims.infra.notification.TipoNotificacion;

import java.util.ArrayList;
import java.util.List;

public class ApplicationModel {
    private Usuario usuarioActual;
    private Scrim scrimSeleccionado;
    private final ScrimService scrimService;
    private final UsuarioRepository usuarioRepository;
    private final NotificationFacade notificationFacade;
    private final List<ModelChangeListener> listeners;

    public ApplicationModel(ScrimService scrimService, UsuarioRepository usuarioRepository, NotificationFacade notificationFacade) {
        this.scrimService = scrimService;
        this.usuarioRepository = usuarioRepository;
        this.notificationFacade = notificationFacade;
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
        
        // Extraer el número del UUID (puede venir como "2" o como "00000000-0000-0000-0000-000000000002")
        String scrimIdStr = scrimId.toString();
        // Si es un UUID formateado, extraer solo el número
        if (scrimIdStr.contains("-")) {
            // Extraer la última parte después del último guión
            String[] parts = scrimIdStr.split("-");
            scrimIdStr = String.valueOf(Long.parseLong(parts[parts.length - 1]));
        }
        
        final String finalScrimIdStr = scrimIdStr;
        System.out.println("[MODEL] Buscando scrim con ID: " + finalScrimIdStr);
        
        Scrim scrim = scrims.stream()
            .filter(s -> s.getId() != null && s.getId().toString().equals(finalScrimIdStr))
            .findFirst()
            .orElse(null);
            
        if (scrim != null) {
            System.out.println("[MODEL] Scrim encontrado: " + scrim.getId());
            Postulacion postulacion = scrimService.postularseAScrim(scrim, usuarioActual, rolDeseado, "");
            
            // PATRÓN FACADE: Notificar al creador del scrim sobre la nueva postulación
            if (scrim.getCreador() != null) {
                String nombreScrim = scrim.getJuego().getNombre() + " - " + scrim.getFormato().getDescripcion();
                String mensaje = String.format(
                    "¡Hola %s! El usuario '%s' se ha postulado a tu scrim '%s' para el rol de %s.\n" +
                    "Ingresa a 'Gestionar Postulaciones' para revisar.",
                    scrim.getCreador().getUsername(),
                    usuarioActual.getUsername(),
                    nombreScrim,
                    rolDeseado.name()
                );
                
                notificationFacade.enviarNotificacion(
                    scrim.getCreador(),
                    mensaje,
                    TipoNotificacion.EMAIL
                );
            }
            
            notifyListeners();
            return postulacion;
        }
        System.err.println("[MODEL] No se encontró scrim con ID: " + finalScrimIdStr);
        return null;
    }

    public List<Postulacion> obtenerPostulacionesDeScrim(java.util.UUID scrimId) {
        List<Scrim> scrims = scrimService.listarTodosScrims();
        
        // Extraer el número del UUID
        String scrimIdStr = scrimId.toString();
        if (scrimIdStr.contains("-")) {
            String[] parts = scrimIdStr.split("-");
            scrimIdStr = String.valueOf(Long.parseLong(parts[parts.length - 1]));
        }
        
        final String finalScrimIdStr = scrimIdStr;
        Scrim scrim = scrims.stream()
            .filter(s -> s.getId() != null && s.getId().toString().equals(finalScrimIdStr))
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
        // Extraer el número del UUID
        String postulacionIdStr = postulacionId.toString();
        if (postulacionIdStr.contains("-")) {
            String[] parts = postulacionIdStr.split("-");
            postulacionIdStr = String.valueOf(Long.parseLong(parts[parts.length - 1]));
        }
        
        final String finalPostulacionIdStr = postulacionIdStr;
        System.out.println("[MODEL] Buscando postulación con ID: " + finalPostulacionIdStr);
        
        // Buscar la postulación en todos los scrims
        for (Scrim scrim : scrimService.listarTodosScrims()) {
            for (Postulacion p : scrim.getPostulaciones()) {
                if (p.getId() != null && p.getId().toString().equals(finalPostulacionIdStr)) {
                    System.out.println("[MODEL] Postulación encontrada, aceptando...");
                    scrimService.aceptarPostulacion(p);
                    
                    // PATRÓN FACADE: Notificar al usuario que su postulación fue aceptada
                    String nombreScrim = scrim.getJuego().getNombre() + " - " + scrim.getFormato().getDescripcion();
                    String mensaje = String.format(
                        "¡Felicidades %s! Tu postulación al scrim '%s' para el rol de %s ha sido ACEPTADA.\n" +
                        "Ya formas parte del equipo. ¡Prepárate para el scrim!",
                        p.getUsuario().getUsername(),
                        nombreScrim,
                        p.getRolSolicitado().name()
                    );
                    
                    notificationFacade.enviarNotificacion(
                        p.getUsuario(),
                        mensaje,
                        TipoNotificacion.EMAIL_PUSH
                    );
                    
                    notifyListeners();
                    return;
                }
            }
        }
        System.err.println("[MODEL] No se encontró postulación con ID: " + finalPostulacionIdStr);
    }

    public void rechazarPostulacion(java.util.UUID postulacionId) {
        // Extraer el número del UUID
        String postulacionIdStr = postulacionId.toString();
        if (postulacionIdStr.contains("-")) {
            String[] parts = postulacionIdStr.split("-");
            postulacionIdStr = String.valueOf(Long.parseLong(parts[parts.length - 1]));
        }
        
        final String finalPostulacionIdStr = postulacionIdStr;
        System.out.println("[MODEL] Buscando postulación para rechazar con ID: " + finalPostulacionIdStr);
        
        // Buscar la postulación en todos los scrims y marcarla como rechazada
        for (Scrim scrim : scrimService.listarTodosScrims()) {
            for (Postulacion p : scrim.getPostulaciones()) {
                if (p.getId() != null && p.getId().toString().equals(finalPostulacionIdStr)) {
                    System.out.println("[MODEL] Postulación encontrada, marcando como rechazada...");
                    p.rechazar();
                    
                    // PATRÓN FACADE: Notificar al usuario que su postulación fue rechazada
                    String nombreScrim = scrim.getJuego().getNombre() + " - " + scrim.getFormato().getDescripcion();
                    String mensaje = String.format(
                        "Hola %s, lamentablemente tu postulación al scrim '%s' para el rol de %s no fue aceptada.\n" +
                        "No te desanimes, hay muchos otros scrims disponibles.",
                        p.getUsuario().getUsername(),
                        nombreScrim,
                        p.getRolSolicitado().name()
                    );
                    
                    notificationFacade.enviarNotificacion(
                        p.getUsuario(),
                        mensaje,
                        TipoNotificacion.EMAIL
                    );
                    
                    notifyListeners();
                    System.out.println("[MODEL] Postulación rechazada exitosamente en scrim " + scrim.getId());
                    return;
                }
            }
        }
        System.err.println("[MODEL] No se encontró postulación con ID: " + finalPostulacionIdStr);
    }

    // Métodos para cambiar estado del Scrim (Patrón STATE)
    public void armarLobbyScrim(java.util.UUID scrimId) {
        String scrimIdStr = scrimId.toString();
        if (scrimIdStr.contains("-")) {
            String[] parts = scrimIdStr.split("-");
            scrimIdStr = String.valueOf(Long.parseLong(parts[parts.length - 1]));
        }
        
        final String finalScrimIdStr = scrimIdStr;
        Scrim scrim = scrimService.listarTodosScrims().stream()
            .filter(s -> s.getId().toString().equals(finalScrimIdStr))
            .findFirst()
            .orElse(null);
        
        if (scrim != null) {
            scrimService.armarLobby(scrim);
            notifyListeners();
            System.out.println("[STATE] Scrim " + scrim.getId() + " cambió a estado: Lobby Armado");
        }
    }

    public void iniciarScrim(java.util.UUID scrimId) {
        String scrimIdStr = scrimId.toString();
        if (scrimIdStr.contains("-")) {
            String[] parts = scrimIdStr.split("-");
            scrimIdStr = String.valueOf(Long.parseLong(parts[parts.length - 1]));
        }
        
        final String finalScrimIdStr = scrimIdStr;
        Scrim scrim = scrimService.listarTodosScrims().stream()
            .filter(s -> s.getId().toString().equals(finalScrimIdStr))
            .findFirst()
            .orElse(null);
        
        if (scrim != null) {
            scrimService.iniciarScrim(scrim);
            notifyListeners();
            System.out.println("[STATE] Scrim " + scrim.getId() + " cambió a estado: En Curso");
        }
    }

    public void finalizarScrim(java.util.UUID scrimId) {
        String scrimIdStr = scrimId.toString();
        if (scrimIdStr.contains("-")) {
            String[] parts = scrimIdStr.split("-");
            scrimIdStr = String.valueOf(Long.parseLong(parts[parts.length - 1]));
        }
        
        final String finalScrimIdStr = scrimIdStr;
        Scrim scrim = scrimService.listarTodosScrims().stream()
            .filter(s -> s.getId().toString().equals(finalScrimIdStr))
            .findFirst()
            .orElse(null);
        
        if (scrim != null) {
            scrimService.finalizarScrim(scrim);
            notifyListeners();
            System.out.println("[STATE] Scrim " + scrim.getId() + " cambió a estado: Finalizado");
        }
    }

    public java.util.Map<String, Integer> obtenerEstadisticas() {
        java.util.Map<String, Integer> stats = new java.util.HashMap<>();
        
        // Estadísticas de scrims
        List<Scrim> todosScrims = scrimService.listarTodosScrims();
        stats.put("totalScrims", todosScrims.size());
        stats.put("scrimsAbiertos", (int) todosScrims.stream().filter(s -> s.getEstado() instanceof BuscandoState).count());
        stats.put("scrimsEnCurso", (int) todosScrims.stream().filter(s -> s.getEstado() instanceof EnCursoState).count());
        stats.put("scrimsFinalizados", (int) todosScrims.stream().filter(s -> s.getEstado() instanceof FinalizadoState).count());
        
        // Estadísticas de usuarios
        stats.put("totalUsuarios", usuarioRepository.buscarTodos().size());
        
        // Estadísticas de postulaciones
        int totalPostulaciones = 0;
        int pendientes = 0;
        int aprobadas = 0;
        int rechazadas = 0;
        
        for (Scrim scrim : todosScrims) {
            List<Postulacion> postulaciones = scrim.getPostulaciones();
            totalPostulaciones += postulaciones.size();
            
            for (Postulacion p : postulaciones) {
                if (p.isAceptada()) {
                    aprobadas++;
                } else if (p.isRechazada()) {
                    rechazadas++;
                } else {
                    pendientes++;
                }
            }
        }
        
        stats.put("totalPostulaciones", totalPostulaciones);
        stats.put("postulacionesPendientes", pendientes);
        stats.put("postulacionesAprobadas", aprobadas);
        stats.put("postulacionesRechazadas", rechazadas);
        
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
