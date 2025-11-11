package com.escrims.model.domain.strategy;

import com.escrims.model.domain.model.Postulacion;
import java.util.List;

public interface SelectionStrategy {
    List<Postulacion> seleccionar(List<Postulacion> postulaciones, int cantidad);
    String getNombre();
}
