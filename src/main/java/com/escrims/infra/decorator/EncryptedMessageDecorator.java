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
    public String getContent() {
        String originalContent = super.getContent();
        return encrypt(originalContent);
    }
    
    @Override
    public String getSubject() {
        String originalSubject = super.getSubject();
        return "[ENCRYPTED] " + originalSubject;
    }
    
    private String encrypt(String text) {
        // Simulacion: simple ROT13 para demo
        StringBuilder encrypted = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                encrypted.append((char) ((c - base + 13) % 26 + base));
            } else {
                encrypted.append(c);
            }
        }
        return encrypted.toString();
    }
}
