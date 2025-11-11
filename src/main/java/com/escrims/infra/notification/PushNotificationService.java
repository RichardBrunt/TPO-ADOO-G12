package com.escrims.infra.notification;

import com.escrims.model.domain.notification.NotificationService;
import com.escrims.model.domain.model.Usuario;

/**
 * Servicio de notificación push (stub para desarrollo).
 */
public class PushNotificationService implements NotificationService {
    
    @Override
    public void enviarNotificacion(Usuario usuario, String mensaje) {
        System.out.println("[PUSH] To: " + usuario.getUsername());
        System.out.println("[PUSH] Message: " + mensaje);
    }
    
    @Override
    public void enviarEmail(Usuario usuario, String asunto, String mensaje) {
        // No implementado para push service
        System.out.println("[PUSH] Email not supported");
    }
    
    @Override
    public void enviarNotificacionPush(Usuario usuario, String titulo, String mensaje) {
        System.out.println("[PUSH] To device: " + usuario.getUsername());
        System.out.println("[PUSH] Title: " + titulo);
        System.out.println("[PUSH] Message: " + mensaje);
    }
}
