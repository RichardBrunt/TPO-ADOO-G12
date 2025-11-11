package com.escrims.infra.decorator;

import com.escrims.model.domain.decorator.Message;

/**
 * ConcreteDecorator - Agrega encriptacion al mensaje.
 */
public class EncryptedMessageDecorator extends MessageDecorator {
    
    public EncryptedMessageDecorator(Message message) {
        super(message);
    }
    
    @Override
    public String getContenido() {
        String originalContent = super.getContenido();
        return encrypt(originalContent);
    }
    
    private String encrypt(String text) {
        // Simulación simple de encriptación (reverse string)
        return new StringBuilder(text).reverse().toString();
    }
}
