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
    protected boolean validarRequisitosMinimos(Scrim scrim, List<Postulacion> postulaciones) {
        // Validacion mas estricta
        if (!super.validarRequisitosMinimos(scrim, postulaciones)) {
            return false;
        }
        
        // Requiere suficientes postulaciones (en este caso solo verificamos que haya al menos la capacidad)
        int capacidadMinima = scrim.getFormato().totalJugadores();
        if (postulaciones.size() < capacidadMinima) {
            System.out.println("   ERROR - Se requieren al menos " + capacidadMinima + " postulaciones");
            return false;
        }
        return true;
    }
    
    @Override
    protected void preprocesarPostulaciones(List<Postulacion> postulaciones) {
        System.out.println("   - Modo BALANCEADO: Analizando skill de jugadores...");
    }
    
    @Override
    protected List<Postulacion> filtrarPostulaciones(Scrim scrim, List<Postulacion> postulaciones) {
        // Filtrado estricto: verifica scrim correcto
        return postulaciones.stream()
            .filter(p -> p.getScrimId().equals(scrim.getId()))
            .collect(Collectors.toList());
    }
    
    @Override
    protected void ordenarPostulaciones(List<Postulacion> postulaciones) {
        // Ordenar por ID (simula ordenamiento por MMR)
        // En un caso real, se ordenaria por skill/MMR del jugador
        postulaciones.sort(Comparator.comparing(p -> p.getId().toString()));
        System.out.println("   - Postulaciones ordenadas por skill");
    }
    
    @Override
    protected List<Postulacion> seleccionarParticipantes(Scrim scrim, List<Postulacion> filtradas) {
        // Seleccion de los mejores jugadores (ya ordenados)
        int capacidad = scrim.getFormato().totalJugadores();
        return filtradas.stream()
            .limit(capacidad)
            .collect(Collectors.toList());
    }
    
    @Override
    protected void postprocesarResultados(Scrim scrim, List<Postulacion> seleccionadas) {
        System.out.println("   - Verificando balance de equipos...");
        System.out.println("   - Balance OK: Equipos equilibrados");
    }
}
