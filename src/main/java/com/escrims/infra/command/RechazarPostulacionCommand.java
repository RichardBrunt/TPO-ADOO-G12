package com.escrims.infra.command;

import com.escrims.model.domain.command.Command;
import com.escrims.model.domain.model.Postulacion;
import com.escrims.model.repository.PostulacionRepository;

import java.util.UUID;

/**
 * ConcreteCommand - Comando para rechazar una postulacion.
 */
public class RechazarPostulacionCommand implements Command {
    
    private final UUID postulacionId;
    private final PostulacionRepository postulacionRepository;
    private Postulacion postulacionBackup;
    
    public RechazarPostulacionCommand(UUID postulacionId, 
                                     PostulacionRepository postulacionRepository) {
        this.postulacionId = postulacionId;
        this.postulacionRepository = postulacionRepository;
    }
    
    @Override
    public void ejecutar() {
        Postulacion postulacion = postulacionRepository.buscarPorId((long) postulacionId.hashCode())
            .orElseThrow(() -> new IllegalArgumentException("Postulacion no encontrada"));
        
        // Backup para undo
        this.postulacionBackup = postulacion;
        
        // Rechazar: eliminar postulacion
        postulacionRepository.eliminar((long) postulacionId.hashCode());
        
        System.out.println(">> Postulacion " + postulacionId + " rechazada");
    }
    
    @Override
    public void deshacer() {
        if (postulacionBackup != null) {
            postulacionRepository.guardar(postulacionBackup);
            System.out.println(">> Rechazo de postulacion " + postulacionId + " revertido");
        }
    }
}
