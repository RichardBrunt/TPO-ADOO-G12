package com.escrims.controller;

import com.escrims.model.domain.model.*;
import com.escrims.model.application.ApplicationModel;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Controlador principal de la aplicaciÃƒÂ³n (PatrÃƒÂ³n MVC - Controller)
 * 
 * Maneja las acciones del usuario y actualiza el modelo.
 * ActÃƒÂºa como intermediario entre las vistas y el modelo.
 */
public class ScrimController {
    
    private final ApplicationModel model;
    
    public ScrimController(ApplicationModel model) {
        this.model = model;
    }
    
    // ============================================================
    // ACCIONES DE AUTENTICACIÃƒâ€œN
    // ============================================================
    
    public boolean handleLogin(String username, String password) {
        try {
            return model.login(username, password);
        } catch (Exception e) {
            System.err.println("Error en login: " + e.getMessage());
            return false;
        }
    }
    
    public void handleLogout() {
        model.logout();
    }
    
    public boolean handleRegistro(String username, String email, String password,
                                 Region region, int mmr, int latencia, Set<Rol> roles) {
        try {
            model.crearUsuario(username, email, password, region, mmr, latencia, roles);
            return true;
        } catch (Exception e) {
            System.err.println("Error en registro: " + e.getMessage());
            return false;
        }
    }
    
    // ============================================================
    // ACCIONES DE SCRIMS
    // ============================================================
    
    public Scrim handleCrearScrim(Juego juego, Formato formato, Region region,
                                  int mmrMin, int mmrMax, int latenciaMax,
                                  LocalDateTime fechaInicio, int duracionMin) {
        try {
            return model.crearScrim(juego, formato, region, mmrMin, mmrMax, 
                                   latenciaMax, fechaInicio, duracionMin);
        } catch (Exception e) {
            System.err.println("Error al crear scrim: " + e.getMessage());
            return null;
        }
    }
    
    public List<Scrim> handleBuscarScrims() {
        try {
            return model.buscarScrimsDisponibles();
        } catch (Exception e) {
            System.err.println("Error al buscar scrims: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    public List<Scrim> handleObtenerTodosScrims() {
        try {
            return model.obtenerTodosScrims();
        } catch (Exception e) {
            System.err.println("Error al obtener scrims: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    // ============================================================
    // ACCIONES DE POSTULACIONES
    // ============================================================
    
    public Postulacion handlePostularse(UUID scrimId, Rol rolDeseado) {
        try {
            return model.postularseAScrim(scrimId, rolDeseado);
        } catch (Exception e) {
            System.err.println("Error al postularse: " + e.getMessage());
            return null;
        }
    }
    
    public List<Postulacion> handleObtenerPostulaciones(UUID scrimId) {
        try {
            return model.obtenerPostulacionesDeScrim(scrimId);
        } catch (Exception e) {
            System.err.println("Error al obtener postulaciones: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    public List<Postulacion> handleObtenerMisPostulaciones() {
        try {
            return model.obtenerMisPostulaciones();
        } catch (Exception e) {
            System.err.println("Error al obtener mis postulaciones: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    public void handleAceptarPostulacion(UUID postulacionId) {
        try {
            model.aceptarPostulacion(postulacionId);
        } catch (Exception e) {
            System.err.println("Error al aceptar postulaciÃƒÂ³n: " + e.getMessage());
        }
    }
    
    public void handleRechazarPostulacion(UUID postulacionId) {
        try {
            model.rechazarPostulacion(postulacionId);
        } catch (Exception e) {
            System.err.println("Error al rechazar postulaciÃƒÂ³n: " + e.getMessage());
        }
    }
    
    // ============================================================
    // INFORMACIÃƒâ€œN DEL SISTEMA
    // ============================================================
    
    public Map<String, Integer> handleObtenerEstadisticas() {
        try {
            return model.obtenerEstadisticas();
        } catch (Exception e) {
            System.err.println("Error al obtener estadÃƒÂ­sticas: " + e.getMessage());
            return Collections.emptyMap();
        }
    }
    
    public Usuario handleObtenerUsuarioActual() {
        return model.getUsuarioActual();
    }
    
    public boolean handleIsLoggedIn() {
        return model.isLoggedIn();
    }
}
