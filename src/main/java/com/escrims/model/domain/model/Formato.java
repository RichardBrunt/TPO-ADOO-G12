package com.escrims.model.domain.model;

public enum Formato {
    CINCO_VS_CINCO("5v5"),
    TRES_VS_TRES("3v3"),
    UNO_VS_UNO("1v1");

    private final String descripcion;

    Formato(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getJugadoresPorEquipo() {
        switch (this) {
            case CINCO_VS_CINCO: return 5;
            case TRES_VS_TRES: return 3;
            case UNO_VS_UNO: return 1;
            default: return 0;
        }
    }
}
