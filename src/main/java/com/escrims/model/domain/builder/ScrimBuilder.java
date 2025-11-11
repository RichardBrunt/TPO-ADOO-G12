package com.escrims.model.domain.builder;

import com.escrims.model.domain.model.*;
import com.escrims.model.domain.strategy.SelectionStrategy;
import java.time.LocalDateTime;

public class ScrimBuilder {
    private String titulo;
    private String descripcion;
    private Juego juego;
    private Formato formato;
    private Region region;
    private int mmrMinimo;
    private int mmrMaximo;
    private LocalDateTime fechaHora;
    private Usuario creador;
    private SelectionStrategy estrategiaSeleccion;

    public ScrimBuilder titulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public ScrimBuilder descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public ScrimBuilder juego(Juego juego) {
        this.juego = juego;
        return this;
    }

    public ScrimBuilder formato(Formato formato) {
        this.formato = formato;
        return this;
    }

    public ScrimBuilder region(Region region) {
        this.region = region;
        return this;
    }

    public ScrimBuilder mmrMinimo(int mmrMinimo) {
        this.mmrMinimo = mmrMinimo;
        return this;
    }

    public ScrimBuilder mmrMaximo(int mmrMaximo) {
        this.mmrMaximo = mmrMaximo;
        return this;
    }

    public ScrimBuilder fechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
        return this;
    }

    public ScrimBuilder creador(Usuario creador) {
        this.creador = creador;
        return this;
    }

    public ScrimBuilder estrategiaSeleccion(SelectionStrategy estrategia) {
        this.estrategiaSeleccion = estrategia;
        return this;
    }

    public Scrim build() {
        Scrim scrim = new Scrim(titulo, descripcion, juego, formato, region, 
                                mmrMinimo, mmrMaximo, fechaHora, creador);
        if (estrategiaSeleccion != null) {
            scrim.setEstrategiaSeleccion(estrategiaSeleccion);
        }
        return scrim;
    }
}
