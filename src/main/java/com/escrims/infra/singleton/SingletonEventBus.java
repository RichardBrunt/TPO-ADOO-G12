package com.escrims.infra.singleton;

import com.escrims.model.domain.events.DomainEvent;
import com.escrims.model.domain.events.DomainEventBus;
import com.escrims.model.domain.events.DomainEventSubscriber;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Patron SINGLETON
 * 
 * Event Bus global con instancia unica en toda la aplicacion.
 * Thread-safe mediante Initialization-on-demand holder idiom.
 * 
 * Garantiza que solo exista una instancia del Event Bus,
 * permitiendo comunicacion centralizada de eventos en toda la app.
 */
public class SingletonEventBus implements DomainEventBus {
    
    /**
     * Holder interno - carga lazy y thread-safe
     * El ClassLoader garantiza que la inicializacion sea thread-safe
     */
    private static class Holder {
        private static final SingletonEventBus INSTANCE = new SingletonEventBus();
    }
    
    /**
     * Obtener la instancia unica del EventBus
     */
    public static SingletonEventBus getInstance() {
        return Holder.INSTANCE;
    }
    
    /**
     * Constructor privado - impide instanciacion externa
     */
    private SingletonEventBus() {
        System.out.println("[SINGLETON] EventBus inicializado - Instancia unica creada");
    }
    
    // Implementacion del EventBus (misma que InMemoryDomainEventBus)
    private final Map<Class<? extends DomainEvent>, List<DomainEventSubscriber<? extends DomainEvent>>> subscribers = new ConcurrentHashMap<>();
    
    @Override
    public <T extends DomainEvent> void publicar(T evento) {
        List<DomainEventSubscriber<? extends DomainEvent>> subs = subscribers.get(evento.getClass());
        if (subs == null) return;
        
        for (DomainEventSubscriber<? extends DomainEvent> s : subs) {
            try {
                @SuppressWarnings("unchecked")
                DomainEventSubscriber<T> subscriber = (DomainEventSubscriber<T>) s;
                subscriber.manejar(evento);
            } catch (Exception ex) {
                System.err.println("Failed to handle event " + evento.getTipo() + ": " + ex.getMessage());
            }
        }
    }
    
    @Override
    public <T extends DomainEvent> void suscribir(DomainEventSubscriber<T> suscriptor) {
        Class<T> eventType = suscriptor.getTipoEvento();
        subscribers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(suscriptor);
    }
    
    public <T extends DomainEvent> void desuscribir(DomainEventSubscriber<T> suscriptor) {
        Class<T> eventType = suscriptor.getTipoEvento();
        List<DomainEventSubscriber<? extends DomainEvent>> list = subscribers.get(eventType);
        if (list != null) list.remove(suscriptor);
    }
    
    /**
     * Metodo adicional para verificar que siempre es la misma instancia
     */
    public String getInstanceId() {
        return "EventBus@" + Integer.toHexString(System.identityHashCode(this));
    }
}
