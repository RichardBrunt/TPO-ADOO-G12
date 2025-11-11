package com.escrims.infra.decorator;

import com.escrims.model.domain.decorator.Message;

/**
 * Decorator base - Implementa Message y tiene referencia a otro Message.
 */
public abstract class MessageDecorator implements Message {
    
    protected Message wrappedMessage;
    
    public MessageDecorator(Message message) {
        this.wrappedMessage = message;
    }
    
    @Override
    public String getContenido() {
        return wrappedMessage.getContenido();
    }
}
