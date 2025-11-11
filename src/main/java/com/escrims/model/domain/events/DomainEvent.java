package com.escrims.model.domain.events;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime ocurridoEn();
    String getTipo();
}
