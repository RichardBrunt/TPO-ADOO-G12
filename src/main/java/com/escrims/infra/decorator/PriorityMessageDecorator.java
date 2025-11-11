package com.escrims.infra.decorator;

import com.escrims.model.domain.decorator.Message;

/**
 * ConcreteDecorator - Agrega prioridad alta al mensaje.
 */
public class PriorityMessageDecorator extends MessageDecorator {
    
    private final String priority;
    
    public PriorityMessageDecorator(Message message, String priority) {
        super(message);
        this.priority = priority;
    }
    
    @Override
    public String getSubject() {
        return "[" + priority + "] " + super.getSubject();
    }
    
    @Override
    public String getContent() {
        return "PRIORIDAD: " + priority + "\n" + 
               "=".repeat(40) + "\n" + 
               super.getContent();
    }
}
