package com.escrims.infra.notification;

import com.escrims.model.domain.notification.NotificationService;
import com.escrims.model.domain.model.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Patrón Facade: Simplifica el envío de notificaciones a través de múltiples canales.
 * Coordina email, push y SMS desde una única interfaz.
 */
public class NotificationFacade {
    private final List<NotificationService> services;
    
    public NotificationFacade() {
        this.services = new ArrayList<>();
    }
    
    /**
     * Registra un servicio de notificación.
     */
    public void registrarServicio(NotificationService service) {
        services.add(service);
    }
    
    /**
     * Envía notificación a través de todos los canales registrados.
     */
    public void notificarATodos(Usuario destinatario, String asunto, String mensaje) {
        for (NotificationService service : services) {
            try {
                service.enviarEmail(destinatario, asunto, mensaje);
            } catch (Exception e) {
                System.err.println("Error al enviar notificación: " + e.getMessage());
            }
        }
    }
    
    /**
     * Envía notificación solo por email.
     */
    public void notificarPorEmail(Usuario destinatario, String asunto, String mensaje) {
        for (NotificationService service : services) {
            service.enviarEmail(destinatario, asunto, mensaje);
            return;
        }
    }
    
    /**
     * Envía notificación solo por push.
     */
    public void notificarPorPush(Usuario destinatario, String titulo, String mensaje) {
        for (NotificationService service : services) {
            service.enviarNotificacionPush(destinatario, titulo, mensaje);
            return;
        }
    }
    
    /**
     * Envía notificación solo por SMS.
     */
    public void notificarPorSMS(Usuario destinatario, String mensaje) {
        for (NotificationService service : services) {
            service.enviarNotificacion(destinatario, mensaje);
            return;
        }
    }
    
    /**
     * Envia notificacion critica (todos los canales disponibles).
     */
    public void notificarCritico(Usuario destinatario, String asunto, String mensaje) {
        System.out.println("\n*** NOTIFICACION CRITICA ***");
        notificarATodos(destinatario, asunto, mensaje);
        System.out.println("*** FIN NOTIFICACION CRITICA ***\n");
    }
    
    /**
     * Retorna la cantidad de canales de notificación disponibles.
     */
    public int getCantidadCanales() {
        return services.size();
    }
}
