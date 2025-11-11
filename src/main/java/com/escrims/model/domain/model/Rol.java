package com.escrims.model.domain.model;

public enum Rol {
    // CS2
    ENTRY_FRAGGER("Entry Fragger"),
    AWP("AWPer"),
    SUPPORT("Support"),
    LURKER("Lurker"),
    IGL("In-Game Leader"),
    
    // LoL
    TOP("Top Laner"),
    JUNGLE("Jungler"),
    MID("Mid Laner"),
    ADC("ADC"),
    SUPPORT_LOL("Support"),
    
    // Valorant
    DUELIST("Duelist"),
    INITIATOR("Initiator"),
    CONTROLLER("Controller"),
    SENTINEL("Sentinel");

    private final String descripcion;

    Rol(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
