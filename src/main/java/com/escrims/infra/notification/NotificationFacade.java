package com.escrims.infra.notification;

import com.escrims.model.domain.notification.NotificationService;

import java.util.ArrayList;
import java.util.List;

/**
 * PatrÃ³n Facade: Simplifica el envÃ­o de notificaciones a travÃ©s de mÃºltiples canales.
 * Coordina email, push y SMS desde una Ãºnica interfaz.
 */
public class NotificationFacade {
    private final List<NotificationService> services;
    
    public NotificationFacade() {
        this.services = new ArrayList<>();
    }
    
    /**
     * Registra un servicio de notificaciÃ³n.
     */
    public void registrarServicio(NotificationService service) {
        services.add(service);
    }
    
    /**
     * EnvÃ­a notificaciÃ³n a travÃ©s de todos los canales registrados.
     */
    public void notificarATodos(String destinatario, String asunto, String mensaje) {
        for (NotificationService service : services) {
            try {
                service.enviar(destinatario, asunto, mensaje);
            } catch (Exception e) {
                System.err.println("Error al enviar notificaciÃ³n por " + service.getTipo() + ": " + e.getMessage());
            }
        }
    }
    
    /**
     * EnvÃ­a notificaciÃ³n solo por email.
     */
    public void notificarPorEmail(String destinatario, String asunto, String mensaje) {
        notificarPorTipo("EMAIL", destinatario, asunto, mensaje);
    }
    
    /**
     * EnvÃ­a notificaciÃ³n solo por push.
     */
    public void notificarPorPush(String destinatario, String asunto, String mensaje) {
        notificarPorTipo("PUSH", destinatario, asunto, mensaje);
    }
    
    /**
     * EnvÃ­a notificaciÃ³n solo por SMS.
     */
    public void notificarPorSMS(String destinatario, String asunto, String mensaje) {
        notificarPorTipo("SMS", destinatario, asunto, mensaje);
    }
    
    /**
     * Envia notificacion critica (todos los canales disponibles).
     */
    public void notificarCritico(String destinatario, String asunto, String mensaje) {
        System.out.println("\n*** NOTIFICACION CRITICA ***");
        notificarATodos(destinatario, asunto, mensaje);
        System.out.println("*** FIN NOTIFICACION CRITICA ***\n");
    }
    
    private void notificarPorTipo(String tipo, String destinatario, String asunto, String mensaje) {
        for (NotificationService service : services) {
            if (service.getTipo().equals(tipo)) {
                service.enviar(destinatario, asunto, mensaje);
                return;
            }
        }
        System.err.println("No hay servicio de notificaciÃ³n configurado para tipo: " + tipo);
    }
    
    /**
     * Retorna la cantidad de canales de notificaciÃ³n disponibles.
     */
    public int cantidadCanales() {
        return services.size();
    }
}
