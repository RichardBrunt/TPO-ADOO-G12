package com.escrims.model.domain.strategy;

import com.escrims.model.domain.model.Postulacion;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ByMMRStrategy implements SelectionStrategy {
    @Override
    public List<Postulacion> seleccionar(List<Postulacion> postulaciones, int cantidad) {
        return postulaciones.stream()
                .sorted(Comparator.comparingInt(p -> {
                    var stats = p.getUsuario().getEstadisticaPorJuego(p.getScrim().getJuego());
                    return stats != null ? -stats.getMmr() : Integer.MAX_VALUE;
                }))
                .limit(cantidad)
                .collect(Collectors.toList());
    }

    @Override
    public String getNombre() {
        return "Selección por MMR";
    }
}
