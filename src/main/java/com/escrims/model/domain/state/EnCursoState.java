package com.escrims.model.domain.state;

import com.escrims.model.domain.model.Scrim;

public class EnCursoState implements ScrimState {
    @Override
    public void ejecutarAccion(Scrim scrim) {
        System.out.println("Scrim en estado EN CURSO - Partida en progreso");
    }

    @Override
    public String getNombre() {
        return "EN CURSO";
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
