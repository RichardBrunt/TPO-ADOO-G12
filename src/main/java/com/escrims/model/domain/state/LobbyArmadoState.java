package com.escrims.model.domain.state;

import com.escrims.model.domain.model.Scrim;

public class LobbyArmadoState implements ScrimState {
    @Override
    public void ejecutarAccion(Scrim scrim) {
        System.out.println("Scrim en estado LOBBY ARMADO - Esperando confirmaciones");
    }

    @Override
    public String getNombre() {
        return "LOBBY ARMADO";
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
