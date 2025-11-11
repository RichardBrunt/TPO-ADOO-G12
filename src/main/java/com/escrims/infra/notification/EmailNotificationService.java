package com.escrims.infra.notification;

import com.escrims.model.domain.notification.NotificationService;
import com.escrims.model.domain.model.Usuario;

/**
 * Servicio de notificación por email (stub para desarrollo).
 */
public class EmailNotificationService implements NotificationService {
    
    @Override
    public void enviarNotificacion(Usuario usuario, String mensaje) {
        System.out.println("[EMAIL] To: " + usuario.getEmail());
        System.out.println("[EMAIL] Message: " + mensaje);
    }
    
    @Override
    public void enviarEmail(Usuario usuario, String asunto, String mensaje) {
        System.out.println("[EMAIL] To: " + usuario.getEmail());
        System.out.println("[EMAIL] Subject: " + asunto);
        System.out.println("[EMAIL] Body: " + mensaje);
    }
    
    @Override
    public void enviarNotificacionPush(Usuario usuario, String titulo, String mensaje) {
        // Email service no soporta push notifications, usar email en su lugar
        enviarEmail(usuario, titulo, mensaje);
    }
}
