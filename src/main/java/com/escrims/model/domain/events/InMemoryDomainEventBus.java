package com.escrims.model.domain.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryDomainEventBus implements DomainEventBus {
    private final Map<Class<?>, List<DomainEventSubscriber<?>>> suscriptores;

    public InMemoryDomainEventBus() {
        this.suscriptores = new HashMap<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends DomainEvent> void publicar(T evento) {
        Class<?> tipoEvento = evento.getClass();
        List<DomainEventSubscriber<?>> suscriptoresDelTipo = suscriptores.get(tipoEvento);
        
        if (suscriptoresDelTipo != null) {
            for (DomainEventSubscriber<?> suscriptor : suscriptoresDelTipo) {
                ((DomainEventSubscriber<T>) suscriptor).manejar(evento);
            }
        }
    }

    @Override
    public <T extends DomainEvent> void suscribir(DomainEventSubscriber<T> suscriptor) {
        Class<T> tipoEvento = suscriptor.getTipoEvento();
        suscriptores.computeIfAbsent(tipoEvento, k -> new ArrayList<>()).add(suscriptor);
    }
}
