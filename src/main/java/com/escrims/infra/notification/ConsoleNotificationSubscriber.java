package com.escrims.infra.notification;

import com.escrims.model.domain.events.DomainEventSubscriber;
import com.escrims.model.domain.events.ScrimStateChanged;

public class ConsoleNotificationSubscriber implements DomainEventSubscriber<ScrimStateChanged> {
    
    @Override
    public void manejar(ScrimStateChanged event) {
        System.out.println("[EVENT] Scrim " + event.getScrim().getId() + " changed " 
            + event.getEstadoAnterior().getNombre() + " -> " + event.getEstadoNuevo().getNombre());
    }
    
    @Override
    public Class<ScrimStateChanged> getTipoEvento() {
        return ScrimStateChanged.class;
    }
}
