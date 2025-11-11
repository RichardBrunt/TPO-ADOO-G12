package com.escrims.infra.notification;

import com.escrims.model.domain.notification.NotificationService;

/**
 * Servicio de notificaciÃ³n SMS (stub para desarrollo).
 */
public class SMSNotificationService implements NotificationService {
    
    @Override
    public void enviar(String destinatario, String asunto, String mensaje) {
        // En producciÃ³n, aquÃ­ irÃ­a integraciÃ³n con servicio SMS real (Twilio, AWS SNS, etc.)
        System.out.println("[SMS] To: " + destinatario);
        System.out.println("[SMS] " + asunto + " - " + mensaje);
    }
    
    @Override
    public String getTipo() {
        return "SMS";
    }
}
