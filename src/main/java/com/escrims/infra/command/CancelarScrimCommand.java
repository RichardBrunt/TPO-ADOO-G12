package com.escrims.infra.command;

import com.escrims.model.domain.command.Command;
import com.escrims.model.domain.model.Scrim;
import com.escrims.model.repository.ScrimRepository;

import java.util.UUID;

/**
 * ConcreteCommand - Comando para cancelar un scrim.
 */
public class CancelarScrimCommand implements Command {
    
    private final UUID scrimId;
    private final ScrimRepository scrimRepository;
    private Scrim scrimBackup;
    
    public CancelarScrimCommand(UUID scrimId, ScrimRepository scrimRepository) {
        this.scrimId = scrimId;
        this.scrimRepository = scrimRepository;
    }
    
    @Override
    public void ejecutar() {
        Scrim scrim = scrimRepository.buscarPorId((long) scrimId.hashCode())
            .orElseThrow(() -> new IllegalArgumentException("Scrim no encontrado"));
        
        // Backup para undo
        this.scrimBackup = scrim;
        
        // Cancelar: eliminar del repositorio
        scrimRepository.eliminar((long) scrimId.hashCode());
        
        System.out.println(">> Scrim " + scrimId + " cancelado");
    }
    
    @Override
    public void deshacer() {
        if (scrimBackup != null) {
            scrimRepository.guardar(scrimBackup);
            System.out.println(">> Cancelacion de scrim " + scrimId + " revertida");
        }
    }
}
