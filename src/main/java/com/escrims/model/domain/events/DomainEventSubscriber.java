package com.escrims.model.domain.events;

public interface DomainEventSubscriber<T extends DomainEvent> {
    void manejar(T evento);
    Class<T> getTipoEvento();
}
