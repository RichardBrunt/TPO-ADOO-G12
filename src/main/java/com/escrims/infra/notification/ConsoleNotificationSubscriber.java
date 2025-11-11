package com.escrims.infra.notification;

import com.escrims.model.domain.events.DomainEventSubscriber;
import com.escrims.model.domain.events.ScrimStateChanged;

/**
 * Subscriber sencillo que loguea eventos a consola (Ãºtil para desarrollo y demos).
 */
public class ConsoleNotificationSubscriber implements DomainEventSubscriber<ScrimStateChanged> {
    @Override
    public void handle(ScrimStateChanged event) {
        System.out.println("[EVENT] Scrim " + event.getAggregateId() + " changed " + event.getPreviousState() + " -> " + event.getNewState());
    }
}
