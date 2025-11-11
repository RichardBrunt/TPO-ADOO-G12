package com.escrims.infra.external;

/**
 * Simulacion de API externa de Riot Games (Adaptee).
 * Esta API tiene su propia interfaz incompatible con nuestro sistema.
 */
public class RiotGamesAPI {
    
    /**
     * Metodo propio de Riot API - retorna objeto complejo.
     */
    public RiotPlayerData fetchPlayerData(String riotId) {
        // Simulacion: en produccion esto haria llamada HTTP real
        return new RiotPlayerData(riotId, 1850, 45, "Gold II");
    }
    
    /**
     * Verifica si existe el jugador en los servidores de Riot.
     */
    public boolean isValidRiotAccount(String riotId) {
        // Simulacion: validacion basica
        return riotId != null && !riotId.isEmpty();
    }
    
    /**
     * Clase interna que representa datos de Riot (estructura diferente a la nuestra).
     */
    public static class RiotPlayerData {
        private final String puuid;
        private final int elo;
        private final int ping;
        private final String tier;
        
        public RiotPlayerData(String puuid, int elo, int ping, String tier) {
            this.puuid = puuid;
            this.elo = elo;
            this.ping = ping;
            this.tier = tier;
        }
        
        public String getPuuid() { return puuid; }
        public int getElo() { return elo; }
        public int getPing() { return ping; }
        public String getTier() { return tier; }
    }
}
