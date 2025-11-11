package com.escrims.infra.decorator;

import com.escrims.model.domain.decorator.Message;

/**
 * ConcreteComponent - Mensaje basico sin decoraciones.
 */
public class SimpleMessage implements Message {
    
    private final String subject;
    private final String content;
    
    public SimpleMessage(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }
    
    @Override
    public String getContent() {
        return content;
    }
    
    @Override
    public String getSubject() {
        return subject;
    }
}
