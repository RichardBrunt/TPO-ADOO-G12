package com.escrims.infra.notification;

import com.escrims.model.domain.events.DomainEventSubscriber;
import com.escrims.model.domain.events.ScrimStateChanged;

/**
 * Subscriber que envía notificaciones multi-canal cuando cambia el estado de un scrim.
 * Usa el NotificationFacade para simplificar el envío.
 */
public class MultiChannelNotificationSubscriber implements DomainEventSubscriber<ScrimStateChanged> {
    private final NotificationFacade notificationFacade;
    
    public MultiChannelNotificationSubscriber(NotificationFacade notificationFacade) {
        this.notificationFacade = notificationFacade;
    }
    
    @Override
    public void manejar(ScrimStateChanged evento) {
        String scrimId = evento.getScrim().getId().toString();
        String mensaje = String.format("El scrim %s cambió de %s a %s",
            scrimId, evento.getEstadoAnterior(), evento.getEstadoNuevo());
        
        // Según el estado, usar diferentes estrategias de notificación
        String estadoNuevo = evento.getEstadoNuevo().toString();
        switch (estadoNuevo) {
            case "LOBBY_ARMADO":
                // Lobby listo: notificar
                System.out.println("[NOTIFICATION - EMAIL/PUSH] Lobby Armado: " + mensaje);
                break;
                
            case "EN_CURSO":
                // Scrim iniciado: notificar por todos los canales
                System.out.println("[NOTIFICATION - ALL CHANNELS] Scrim Iniciado: " + mensaje);
                break;
                
            case "FINALIZADO":
                // Scrim terminado: solo email para resumen
                System.out.println("[NOTIFICATION - EMAIL] Scrim Finalizado: " + mensaje);
                break;
                
            default:
                // Para otros estados, solo consola
                System.out.println("[NOTIFICATION] " + mensaje);
        }
    }
    
    @Override
    public Class<ScrimStateChanged> getTipoEvento() {
        return ScrimStateChanged.class;
    }
}
