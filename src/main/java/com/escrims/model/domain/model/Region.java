package com.escrims.model.domain.model;

public enum Region {
    LAS("Latinoamérica Sur"),
    LAN("Latinoamérica Norte"),
    NA("Norteamérica"),
    EU("Europa"),
    BR("Brasil");

    private final String nombre;

    Region(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
