package com.escrims.model.domain.events;

import com.escrims.model.domain.model.Scrim;
import com.escrims.model.domain.state.ScrimState;
import java.time.LocalDateTime;

public class ScrimStateChanged implements DomainEvent {
    private final Scrim scrim;
    private final ScrimState estadoAnterior;
    private final ScrimState estadoNuevo;
    private final LocalDateTime timestamp;

    public ScrimStateChanged(Scrim scrim, ScrimState estadoAnterior, ScrimState estadoNuevo) {
        this.scrim = scrim;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.timestamp = LocalDateTime.now();
    }

    public Scrim getScrim() {
        return scrim;
    }

    public ScrimState getEstadoAnterior() {
        return estadoAnterior;
    }

    public ScrimState getEstadoNuevo() {
        return estadoNuevo;
    }

    @Override
    public LocalDateTime ocurridoEn() {
        return timestamp;
    }

    @Override
    public String getTipo() {
        return "ScrimStateChanged";
    }
}
