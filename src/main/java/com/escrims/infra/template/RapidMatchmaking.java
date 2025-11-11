package com.escrims.infra.template;

import com.escrims.model.domain.model.Scrim;
import com.escrims.model.domain.model.Postulacion;
import com.escrims.model.domain.template.ScrimMatchmakingTemplate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementacion concreta del Template Method para matchmaking RAPIDO
 * 
 * Prioriza velocidad sobre precision:
 * - Validacion minima
 * - Sin ordenamiento complejo
 * - Seleccion por orden de llegada (FIFO)
 */
public class RapidMatchmaking extends ScrimMatchmakingTemplate {
    
    @Override
    protected void validarScrim(Scrim scrim) {
        if (scrim == null) {
            throw new IllegalArgumentException("El scrim no puede ser null");
        }
        System.out.println("   - Modo RAPIDO: Validación mínima");
    }
    
    @Override
    protected List<Postulacion> filtrarPostulaciones(Scrim scrim) {
        // En una implementación real, buscaría las postulaciones del scrim
        System.out.println("   - Filtrando postulaciones (modo rápido)...");
        return List.of();
    }
    
    @Override
    protected List<Postulacion> ordenarPostulaciones(List<Postulacion> postulaciones) {
        // Sin ordenamiento - mantiene orden de llegada (FIFO)
        System.out.println("   - Sin ordenamiento (FIFO)");
        return postulaciones;
    }
    
    @Override
    protected List<Postulacion> seleccionarJugadores(Scrim scrim, List<Postulacion> postulaciones) {
        // Selección simple: primeros N jugadores (FIFO)
        System.out.println("   - Seleccionando primeros jugadores (FIFO)...");
        return postulaciones.stream()
            .limit(scrim.getFormato().getJugadoresPorEquipo() * 2) // 2 equipos
            .collect(Collectors.toList());
    }
    
    @Override
    protected void notificarResultados(Scrim scrim, List<Postulacion> seleccionadas) {
        System.out.println("   - Notificando resultados (modo rápido)...");
        System.out.println("   - " + seleccionadas.size() + " jugadores seleccionados");
    }
}
