package com.escrims.infra.command;

import com.escrims.model.domain.command.Command;

import java.util.Stack;

/**
 * Invoker - Ejecuta comandos y mantiene un historial para undo.
 */
public class CommandInvoker {
    
    private final Stack<Command> commandHistory = new Stack<>();
    
    /**
     * Ejecuta un comando y lo guarda en el historial.
     */
    public void executeCommand(Command command) {
        command.execute();
        commandHistory.push(command);
    }
    
    /**
     * Deshace el ultimo comando ejecutado.
     */
    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            Command lastCommand = commandHistory.pop();
            lastCommand.undo();
        } else {
            System.out.println(">> No hay comandos para deshacer");
        }
    }
    
    /**
     * Muestra el historial de comandos ejecutados.
     */
    public void showHistory() {
        System.out.println("\n>> Historial de comandos:");
        if (commandHistory.isEmpty()) {
            System.out.println("   (vacio)");
        } else {
            for (int i = 0; i < commandHistory.size(); i++) {
                System.out.println("   " + (i + 1) + ". " + commandHistory.get(i).getDescription());
            }
        }
    }
    
    /**
     * Limpia el historial de comandos.
     */
    public void clearHistory() {
        commandHistory.clear();
    }
}
