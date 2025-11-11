package com.escrims.model.domain.factory;

import com.escrims.model.domain.model.*;
import java.time.LocalDateTime;

public class ValorantScrimFactory implements ScrimFactory {
    @Override
    public Scrim crearScrim(String titulo, String descripcion, Formato formato,
                           Region region, int mmrMinimo, int mmrMaximo,
                           LocalDateTime fechaHora, Usuario creador) {
        return new Scrim(titulo, descripcion, Juego.VALORANT, formato, region,
                        mmrMinimo, mmrMaximo, fechaHora, creador);
    }

    @Override
    public Juego getJuego() {
        return Juego.VALORANT;
    }
}
