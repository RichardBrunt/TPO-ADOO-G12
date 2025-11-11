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
    protected List<Postulacion> filtrarPostulaciones(Scrim scrim, List<Postulacion> postulaciones) {
        // Filtrado basico: solo verifica que la postulacion sea del scrim correcto
        return postulaciones.stream()
            .filter(p -> p.getScrimId().equals(scrim.getId()))
            .collect(Collectors.toList());
    }
    
    @Override
    protected List<Postulacion> seleccionarParticipantes(Scrim scrim, List<Postulacion> filtradas) {
        // Seleccion simple: primeros N jugadores (FIFO)
        int capacidad = scrim.getFormato().totalJugadores();
        return filtradas.stream()
            .limit(capacidad)
            .collect(Collectors.toList());
    }
    
    @Override
    protected void preprocesarPostulaciones(List<Postulacion> postulaciones) {
        System.out.println("   - Modo RAPIDO: Sin pre-procesamiento adicional");
    }
}
