package com.escrims.controller;

import com.escrims.model.domain.model.*;
import com.escrims.model.domain.strategy.*;
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
                                  String estrategia, int mmrMin, int mmrMax, int latenciaMax,
                                  LocalDateTime fechaInicio, int duracionMin) {
        try {
            // Crear la estrategia de selección según la opción elegida
            SelectionStrategy strategy;
            switch (estrategia) {
                case "Por MMR":
                    strategy = new ByMMRStrategy();
                    System.out.println("[STRATEGY] Usando estrategia: Por MMR (selecciona jugadores con mayor MMR)");
                    break;
                case "Por Latencia":
                    strategy = new ByLatenciaStrategy();
                    System.out.println("[STRATEGY] Usando estrategia: Por Latencia (selecciona jugadores con menor latencia)");
                    break;
                case "Por Historial (FIFO)":
                    strategy = new FIFOStrategy();
                    System.out.println("[STRATEGY] Usando estrategia: FIFO (primer postulado, primer seleccionado)");
                    break;
                default:
                    strategy = new ByMMRStrategy(); // Por defecto MMR
                    System.out.println("[STRATEGY] Usando estrategia por defecto: Por MMR");
                    break;
            }
            
            ApplicationModel.ScrimFormData formData = new ApplicationModel.ScrimFormData();
            formData.setTitulo("Scrim " + juego.getNombre());
            formData.setDescripcion("Scrim creado desde controller");
            formData.setJuego(juego);
            formData.setFormato(formato);
            formData.setRegion(region);
            formData.setMmrMinimo(mmrMin);
            formData.setMmrMaximo(mmrMax);
            formData.setFechaHora(fechaInicio);
            formData.setEstrategiaSeleccion(strategy);
            
            Scrim scrim = model.crearScrim(formData);
            
            if (scrim != null) {
                System.out.println("[STRATEGY] Scrim creado con estrategia: " + strategy.getNombre());
            }
            
            return scrim;
        } catch (Exception e) {
            System.err.println("Error al crear scrim: " + e.getMessage());
            e.printStackTrace();
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
            System.out.println("[CONTROLLER] handlePostularse - ScrimID: " + scrimId + ", Rol: " + rolDeseado);
            Postulacion postulacion = model.postularseAScrim(scrimId, rolDeseado);
            
            if (postulacion != null) {
                System.out.println("[CONTROLLER] Postulación creada exitosamente - ID: " + postulacion.getId());
            } else {
                System.err.println("[CONTROLLER] La postulación retornó NULL");
            }
            
            return postulacion;
        } catch (Exception e) {
            System.err.println("[CONTROLLER ERROR] Error al postularse: " + e.getMessage());
            e.printStackTrace();
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
