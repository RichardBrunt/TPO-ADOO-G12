package com.escrims.infra.adapter;

import com.escrims.model.domain.adapter.PlayerStatsProvider;
import com.escrims.infra.external.RiotGamesAPI;
import com.escrims.infra.external.RiotGamesAPI.RiotPlayerData;

/**
 * Patron Adapter: Adapta la API de Riot Games a nuestra interfaz PlayerStatsProvider.
 * Convierte las llamadas de nuestra interfaz a las llamadas especificas de Riot API.
 */
public class RiotAPIAdapter implements PlayerStatsProvider {
    
    private final RiotGamesAPI riotAPI;
    
    public RiotAPIAdapter() {
        this.riotAPI = new RiotGamesAPI();
    }
    
    @Override
    public int getPlayerRanking(String playerId) {
        RiotPlayerData data = riotAPI.fetchPlayerData(playerId);
        // Adaptacion: Riot usa "elo", nosotros usamos "ranking/MMR"
        return data.getElo();
    }
    
    @Override
    public int getPlayerLatency(String playerId) {
        RiotPlayerData data = riotAPI.fetchPlayerData(playerId);
        // Adaptacion: Riot usa "ping", nosotros usamos "latency"
        return data.getPing();
    }
    
    @Override
    public String getProviderName() {
        return "Riot Games API Adapter (Valorant/LoL)";
    }
    
    @Override
    public boolean playerExists(String playerId) {
        // Adaptacion: usamos el metodo de validacion de Riot
        return riotAPI.isValidRiotAccount(playerId);
    }
}
