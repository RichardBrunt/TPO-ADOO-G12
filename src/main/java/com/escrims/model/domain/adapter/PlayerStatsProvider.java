package com.escrims.model.domain.adapter;

import com.escrims.model.domain.model.Estadistica;
import com.escrims.model.domain.model.Juego;

public interface PlayerStatsProvider {
    Estadistica obtenerEstadisticas(String username, Juego juego);
}
