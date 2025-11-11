package com.escrims.infra.adapter;

import com.escrims.model.domain.adapter.PlayerStatsProvider;
import com.escrims.model.domain.model.Estadistica;
import com.escrims.model.domain.model.Juego;

/**
 * Patron Adapter: Adapta la API de Steam a nuestra interfaz PlayerStatsProvider.
 * Simula la integración con Steam API (CS2).
 */
public class SteamAPIAdapter implements PlayerStatsProvider {
    
    @Override
    public Estadistica obtenerEstadisticas(String username, Juego juego) {
        // Simulación de llamada a Steam API
        // En producción, aquí iría la llamada real a la API de Steam
        int mmr = (int) (Math.random() * 2500) + 500; // MMR entre 500-3000
        int partidasJugadas = (int) (Math.random() * 200) + 100;
        double winRate = 45 + (Math.random() * 15); // 45-60%
        int latencia = (int) (Math.random() * 60) + 15; // 15-75ms
        
        return new Estadistica(juego, mmr, partidasJugadas, winRate, latencia);
    }
}
