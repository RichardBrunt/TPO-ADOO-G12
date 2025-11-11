package com.escrims.model.domain.state;

import com.escrims.model.domain.model.Scrim;

public class FinalizadoState implements ScrimState {
    @Override
    public void ejecutarAccion(Scrim scrim) {
        System.out.println("Scrim en estado FINALIZADO - Partida completada");
    }

    @Override
    public String getNombre() {
        return "FINALIZADO";
    }

    @Override
    public boolean puedeAceptarPostulaciones() {
        return false;
    }

    @Override
    public String toString() {
        return getNombre();
    }
}
