package com.escrims.model.domain.model;

public class Estadistica {
    private final Juego juego;
    private final int mmr;
    private final int partidasJugadas;
    private final double winRate;
    private final int latenciaPromedio;

    public Estadistica(Juego juego, int mmr, int partidasJugadas, double winRate, int latenciaPromedio) {
        this.juego = juego;
        this.mmr = mmr;
        this.partidasJugadas = partidasJugadas;
        this.winRate = winRate;
        this.latenciaPromedio = latenciaPromedio;
    }

    public Juego getJuego() {
        return juego;
    }

    public int getMmr() {
        return mmr;
    }

    public int getPartidasJugadas() {
        return partidasJugadas;
    }

    public double getWinRate() {
        return winRate;
    }

    public int getLatenciaPromedio() {
        return latenciaPromedio;
    }

    @Override
    public String toString() {
        return String.format("%s - MMR: %d, Partidas: %d, WR: %.1f%%, Latencia: %dms",
                juego.getNombre(), mmr, partidasJugadas, winRate, latenciaPromedio);
    }
}
