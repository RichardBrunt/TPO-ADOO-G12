package com.escrims.infra.external;

import java.util.HashMap;
import java.util.Map;

/**
 * Simulacion de API externa de Steam (para CS2).
 * Esta API tiene metodos y estructura completamente diferentes.
 */
public class SteamAPI {
    
    /**
     * Steam usa SteamID64 y retorna Map con datos.
     */
    public Map<String, Object> getUserStats(String steamId64) {
        // Simulacion: en produccion esto haria llamada HTTP a Steam Web API
        Map<String, Object> stats = new HashMap<>();
        stats.put("steamid", steamId64);
        stats.put("skill_level", 2200); // Rating de CS2
        stats.put("average_ping", 25);
        stats.put("rank", "Global Elite");
        stats.put("hours_played", 1500);
        return stats;
    }
    
    /**
     * Verifica perfil de Steam.
     */
    public boolean validateSteamProfile(String steamId64) {
        // Simulacion: validacion basica
        return steamId64 != null && steamId64.matches("\\d{17}");
    }
    
    /**
     * Obtiene latencia desde servidores de Steam.
     */
    public int getPingToSteamServers(String steamId64) {
        // Simulacion
        return 25;
    }
}
