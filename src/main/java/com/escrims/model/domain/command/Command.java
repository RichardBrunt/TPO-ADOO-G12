package com.escrims.model.domain.command;

public interface Command {
    void ejecutar();
    void deshacer();
}
