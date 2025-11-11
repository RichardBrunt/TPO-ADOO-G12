package com.escrims.infra.adapter;

import com.escrims.model.domain.adapter.PlayerStatsProvider;
import com.escrims.infra.external.SteamAPI;

import java.util.Map;

/**
 * Patron Adapter: Adapta la API de Steam a nuestra interfaz PlayerStatsProvider.
 * Steam tiene estructura de datos y metodos completamente diferentes.
 */
public class SteamAPIAdapter implements PlayerStatsProvider {
    
    private final SteamAPI steamAPI;
    
    public SteamAPIAdapter() {
        this.steamAPI = new SteamAPI();
    }
    
    @Override
    public int getPlayerRanking(String playerId) {
        Map<String, Object> stats = steamAPI.getUserStats(playerId);
        // Adaptacion: Steam usa "skill_level" en un Map, nosotros usamos metodo directo
        Object skillLevel = stats.get("skill_level");
        return skillLevel != null ? (Integer) skillLevel : 0;
    }
    
    @Override
    public int getPlayerLatency(String playerId) {
        // Adaptacion: Steam tiene metodo separado para ping
        return steamAPI.getPingToSteamServers(playerId);
    }
    
    @Override
    public String getProviderName() {
        return "Steam API Adapter (CS2)";
    }
    
    @Override
    public boolean playerExists(String playerId) {
        // Adaptacion: usamos el metodo de validacion de Steam
        return steamAPI.validateSteamProfile(playerId);
    }
}
