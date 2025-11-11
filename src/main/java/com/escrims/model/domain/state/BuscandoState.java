package com.escrims.model.domain.state;

import com.escrims.model.domain.model.Scrim;

public class BuscandoState implements ScrimState {
    @Override
    public void ejecutarAccion(Scrim scrim) {
        System.out.println("Scrim en estado BUSCANDO - Aceptando postulaciones");
    }

    @Override
    public String getNombre() {
        return "BUSCANDO";
    }

    @Override
    public boolean puedeAceptarPostulaciones() {
        return true;
    }

    @Override
    public String toString() {
        return getNombre();
    }
}
