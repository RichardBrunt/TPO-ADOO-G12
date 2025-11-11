package com.escrims.model.domain.factory;

import com.escrims.model.domain.model.*;
import java.time.LocalDateTime;

public interface ScrimFactory {
    Scrim crearScrim(String titulo, String descripcion, Formato formato, 
                     Region region, int mmrMinimo, int mmrMaximo, 
                     LocalDateTime fechaHora, Usuario creador);
    Juego getJuego();
}
