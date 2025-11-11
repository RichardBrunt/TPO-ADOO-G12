package com.escrims.infra.adapter;

import com.escrims.model.domain.adapter.PlayerStatsProvider;
import com.escrims.model.domain.model.Estadistica;
import com.escrims.model.domain.model.Juego;

/**
 * Patron Adapter: Adapta la API de Riot Games a nuestra interfaz PlayerStatsProvider.
 * Simula la integración con Riot API (Valorant/LoL).
 */
public class RiotAPIAdapter implements PlayerStatsProvider {
    
    @Override
    public Estadistica obtenerEstadisticas(String username, Juego juego) {
        // Simulación de llamada a Riot API
        // En producción, aquí iría la llamada real a la API de Riot Games
        int mmr = (int) (Math.random() * 2000) + 1000; // MMR entre 1000-3000
        int partidasJugadas = (int) (Math.random() * 100) + 50;
        double winRate = 40 + (Math.random() * 20); // 40-60%
        int latencia = (int) (Math.random() * 50) + 20; // 20-70ms
        
        return new Estadistica(juego, mmr, partidasJugadas, winRate, latencia);
    }
}
