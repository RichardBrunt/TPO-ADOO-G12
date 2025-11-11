package com.escrims.infra.notification;

import com.escrims.model.domain.notification.NotificationService;

/**
 * Servicio de notificaciÃ³n push (stub para desarrollo).
 */
public class PushNotificationService implements NotificationService {
    
    @Override
    public void enviar(String destinatario, String asunto, String mensaje) {
        // En producciÃ³n, aquÃ­ irÃ­a integraciÃ³n con servicio push real (Firebase, OneSignal, etc.)
        System.out.println("[PUSH] To device: " + destinatario);
        System.out.println("[PUSH] Title: " + asunto);
        System.out.println("[PUSH] Message: " + mensaje);
    }
    
    @Override
    public String getTipo() {
        return "PUSH";
    }
}
