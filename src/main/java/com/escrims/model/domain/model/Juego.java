package com.escrims.model.domain.model;

public enum Juego {
    CS2("Counter-Strike 2"),
    LOL("League of Legends"),
    VALORANT("Valorant");

    private final String nombre;

    Juego(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
