package com.escrims.infra.notification;

import com.escrims.model.domain.events.DomainEventSubscriber;
import com.escrims.model.domain.events.ScrimStateChanged;

/**
 * Subscriber que envÃ­a notificaciones multi-canal cuando cambia el estado de un scrim.
 * Usa el NotificationFacade para simplificar el envÃ­o.
 */
public class MultiChannelNotificationSubscriber implements DomainEventSubscriber<ScrimStateChanged> {
    private final NotificationFacade notificationFacade;
    
    public MultiChannelNotificationSubscriber(NotificationFacade notificationFacade) {
        this.notificationFacade = notificationFacade;
    }
    
    @Override
    public void handle(ScrimStateChanged event) {
        String scrimId = event.getAggregateId().toString().substring(0, 8);
        String mensaje = String.format("El scrim %s cambiÃ³ de %s a %s",
            scrimId, event.getPreviousState(), event.getNewState());
        
        // SegÃºn el estado, usar diferentes estrategias de notificaciÃ³n
        switch (event.getNewState()) {
            case "LOBBY_ARMADO":
                // Lobby listo: notificar por email y push
                notificationFacade.notificarPorEmail("admin@escrims.com", 
                    "Lobby Armado", mensaje);
                notificationFacade.notificarPorPush("all-players", 
                    "Â¡Lobby listo!", mensaje);
                break;
                
            case "EN_CURSO":
                // Scrim iniciado: notificar por todos los canales
                notificationFacade.notificarATodos("participants", 
                    "Scrim Iniciado", mensaje);
                break;
                
            case "FINALIZADO":
                // Scrim terminado: solo email para resumen
                notificationFacade.notificarPorEmail("admin@escrims.com", 
                    "Scrim Finalizado", mensaje);
                break;
                
            default:
                // Para otros estados, solo consola
                System.out.println("[NOTIFICATION] " + mensaje);
        }
    }
}
