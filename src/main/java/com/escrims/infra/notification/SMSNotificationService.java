package com.escrims.infra.notification;

import com.escrims.model.domain.notification.NotificationService;
import com.escrims.model.domain.model.Usuario;

/**
 * Servicio de notificación SMS (stub para desarrollo).
 */
public class SMSNotificationService implements NotificationService {
    
    @Override
    public void enviarNotificacion(Usuario usuario, String mensaje) {
        System.out.println("[SMS] To: " + usuario.getUsername());
        System.out.println("[SMS] Message: " + mensaje);
    }
    
    @Override
    public void enviarEmail(Usuario usuario, String asunto, String mensaje) {
        // SMS service no soporta email, usar SMS simple
        enviarNotificacion(usuario, asunto + " - " + mensaje);
    }
    
    @Override
    public void enviarNotificacionPush(Usuario usuario, String titulo, String mensaje) {
        System.out.println("[SMS] To: " + usuario.getUsername());
        System.out.println("[SMS] " + titulo + ": " + mensaje);
    }
}
