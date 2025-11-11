package com.escrims.model.domain.events;

public interface DomainEventBus {
    <T extends DomainEvent> void publicar(T evento);
    <T extends DomainEvent> void suscribir(DomainEventSubscriber<T> suscriptor);
}
