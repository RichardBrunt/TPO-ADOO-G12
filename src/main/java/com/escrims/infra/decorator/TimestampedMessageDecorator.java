package com.escrims.infra.decorator;

import com.escrims.model.domain.decorator.Message;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ConcreteDecorator - Agrega timestamp al mensaje.
 */
public class TimestampedMessageDecorator extends MessageDecorator {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public TimestampedMessageDecorator(Message message) {
        super(message);
    }
    
    @Override
    public String getContent() {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        return "[" + timestamp + "] " + super.getContent();
    }
}
