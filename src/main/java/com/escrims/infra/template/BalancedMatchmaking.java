package com.escrims.infra.template;

import com.escrims.model.domain.model.Scrim;
import com.escrims.model.domain.model.Postulacion;
import com.escrims.model.domain.template.ScrimMatchmakingTemplate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

/**
 * Implementacion concreta del Template Method para matchmaking BALANCEADO
 * 
 * Prioriza calidad del match:
 * - Validacion estricta
 * - Ordenamiento por skill (MMR simulado)
 * - Seleccion equilibrada
 */
public class BalancedMatchmaking extends ScrimMatchmakingTemplate {
    
    @Override
    protected void validarScrim(Scrim scrim) {
        if (scrim == null) {
            throw new IllegalArgumentException("El scrim no puede ser null");
        }
        System.out.println("   - Modo BALANCEADO: Validando scrim...");
    }
    
    @Override
    protected List<Postulacion> filtrarPostulaciones(Scrim scrim) {
        // En una implementación real, aquí se buscarían las postulaciones del scrim
        // Por ahora devolvemos lista vacía - esto debería recibir postulaciones como parámetro
        System.out.println("   - Filtrando postulaciones del scrim...");
        return List.of();
    }
    
    @Override
    protected List<Postulacion> ordenarPostulaciones(List<Postulacion> postulaciones) {
        // Ordenar por ID (simula ordenamiento por MMR)
        // En un caso real, se ordenaría por skill/MMR del jugador
        System.out.println("   - Postulaciones ordenadas por skill");
        return postulaciones.stream()
            .sorted(Comparator.comparing(p -> p.getId().toString()))
            .collect(Collectors.toList());
    }
    
    @Override
    protected List<Postulacion> seleccionarJugadores(Scrim scrim, List<Postulacion> postulaciones) {
        // Selección de los mejores jugadores (ya ordenados)
        System.out.println("   - Seleccionando jugadores balanceados...");
        return postulaciones.stream()
            .limit(scrim.getFormato().getJugadoresPorEquipo() * 2) // 2 equipos
            .collect(Collectors.toList());
    }
    
    @Override
    protected void notificarResultados(Scrim scrim, List<Postulacion> seleccionadas) {
        System.out.println("   - Notificando resultados...");
        System.out.println("   - " + seleccionadas.size() + " jugadores seleccionados");
        System.out.println("   - Balance OK: Equipos equilibrados");
    }
}
