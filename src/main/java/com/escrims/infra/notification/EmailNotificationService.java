package com.escrims.infra.notification;

import com.escrims.model.domain.notification.NotificationService;

/**
 * Servicio de notificaciÃ³n por email (stub para desarrollo).
 */
public class EmailNotificationService implements NotificationService {
    
    @Override
    public void enviar(String destinatario, String asunto, String mensaje) {
        // En producciÃ³n, aquÃ­ irÃ­a integraciÃ³n con servicio de email real
        System.out.println("[EMAIL] To: " + destinatario);
        System.out.println("[EMAIL] Subject: " + asunto);
        System.out.println("[EMAIL] Body: " + mensaje);
    }
    
    @Override
    public String getTipo() {
        return "EMAIL";
    }
}
