package com.escrims.model.domain.state;

import com.escrims.model.domain.model.Scrim;

public interface ScrimState {
    void ejecutarAccion(Scrim scrim);
    String getNombre();
    boolean puedeAceptarPostulaciones();
}
