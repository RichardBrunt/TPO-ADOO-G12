package com.escrims.infra.notification;

import com.escrims.model.domain.notification.NotificationService;
import com.escrims.model.domain.model.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Patrón FACADE: Unifica múltiples sistemas de notificación bajo una interfaz simple y centralizada.
 * 
 * Integra canales como EMAIL, SMS, PUSH NOTIFICATIONS y WEBHOOKS.
 * Expone un único método público enviarNotificacion(destinatario, mensaje, tipo) 
 * que abstrae toda la lógica interna de selección y envío por múltiples canales.
 * 
 * El cliente no necesita conocer los detalles de cada servicio de notificación.
 */
public class NotificationFacade {
    private final List<NotificationService> services;
    
    public NotificationFacade() {
        this.services = new ArrayList<>();
        System.out.println("[FACADE] Notification Facade inicializado");
    }
    
    /**
     * Registra un servicio de notificación (Email, Push, SMS, etc.).
     * Método de configuración interna.
     */
    public void registrarServicio(NotificationService service) {
        services.add(service);
        System.out.println("[FACADE] Servicio registrado: " + service.getClass().getSimpleName());
    }
    
    /**
     * MÉTODO PÚBLICO PRINCIPAL (Patrón FACADE)
     * 
     * Envía notificación al destinatario a través de los canales especificados.
     * Este es el único punto de entrada que necesita conocer el cliente.
     * 
     * @param destinatario Usuario que recibirá la notificación
     * @param mensaje Contenido del mensaje a enviar
     * @param tipo Tipo de notificación (EMAIL, PUSH, SMS, EMAIL_PUSH, TODOS)
     */
    public void enviarNotificacion(Usuario destinatario, String mensaje, TipoNotificacion tipo) {
        System.out.println("\n[FACADE] Procesando notificación tipo: " + tipo);
        System.out.println("[FACADE] Destinatario: " + destinatario.getUsername());
        
        if (services.isEmpty()) {
            System.err.println("[FACADE] ERROR: No hay servicios de notificación registrados");
            return;
        }
        
        switch (tipo) {
            case EMAIL:
                enviarPorEmail(destinatario, mensaje);
                break;
                
            case PUSH:
                enviarPorPush(destinatario, mensaje);
                break;
                
            case SMS:
                enviarPorSMS(destinatario, mensaje);
                break;
                
            case EMAIL_PUSH:
                enviarPorEmail(destinatario, mensaje);
                enviarPorPush(destinatario, mensaje);
                break;
                
            case TODOS:
                enviarPorTodosLosCanales(destinatario, mensaje);
                break;
                
            default:
                System.err.println("[FACADE] Tipo de notificación desconocido: " + tipo);
        }
        
        System.out.println("[FACADE] Notificación procesada exitosamente\n");
    }
    
    // ========== MÉTODOS PRIVADOS (LÓGICA INTERNA DEL FACADE) ==========
    
    /**
     * Envía el mensaje solo por email.
     * Método privado - el cliente no necesita conocer esta implementación.
     */
    private void enviarPorEmail(Usuario destinatario, String mensaje) {
        String asunto = "Notificación eScrims";
        for (NotificationService service : services) {
            try {
                service.enviarEmail(destinatario, asunto, mensaje);
                return; // Solo usamos el primer servicio disponible
            } catch (Exception e) {
                System.err.println("[FACADE] Error al enviar email: " + e.getMessage());
            }
        }
    }
    
    /**
     * Envía el mensaje solo por push notification.
     * Método privado - el cliente no necesita conocer esta implementación.
     */
    private void enviarPorPush(Usuario destinatario, String mensaje) {
        String titulo = "eScrims";
        for (NotificationService service : services) {
            try {
                service.enviarNotificacionPush(destinatario, titulo, mensaje);
                return;
            } catch (Exception e) {
                System.err.println("[FACADE] Error al enviar push: " + e.getMessage());
            }
        }
    }
    
    /**
     * Envía el mensaje solo por SMS.
     * Método privado - el cliente no necesita conocer esta implementación.
     */
    private void enviarPorSMS(Usuario destinatario, String mensaje) {
        for (NotificationService service : services) {
            try {
                service.enviarNotificacion(destinatario, mensaje);
                return;
            } catch (Exception e) {
                System.err.println("[FACADE] Error al enviar SMS: " + e.getMessage());
            }
        }
    }
    
    /**
     * Envía el mensaje por TODOS los canales disponibles.
     * Método privado para notificaciones críticas.
     */
    private void enviarPorTodosLosCanales(Usuario destinatario, String mensaje) {
        System.out.println("[FACADE] *** NOTIFICACIÓN CRÍTICA - TODOS LOS CANALES ***");
        enviarPorEmail(destinatario, mensaje);
        enviarPorPush(destinatario, mensaje);
        enviarPorSMS(destinatario, mensaje);
    }
    
    /**
     * Retorna la cantidad de canales de notificación disponibles.
     */
    public int getCantidadCanales() {
        return services.size();
    }
}
