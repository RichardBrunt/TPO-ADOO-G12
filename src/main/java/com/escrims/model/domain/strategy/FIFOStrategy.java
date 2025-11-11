package com.escrims.model.domain.strategy;

import com.escrims.model.domain.model.Postulacion;
import java.util.List;
import java.util.stream.Collectors;

public class FIFOStrategy implements SelectionStrategy {
    @Override
    public List<Postulacion> seleccionar(List<Postulacion> postulaciones, int cantidad) {
        return postulaciones.stream()
                .limit(cantidad)
                .collect(Collectors.toList());
    }

    @Override
    public String getNombre() {
        return "Primero en llegar, primero en ser atendido (FIFO)";
    }
}
