package com.escrims.model.domain.model;

import java.time.LocalDateTime;

public class Confirmacion {
    private Long id;
    private Usuario usuario;
    private boolean confirmado;
    private LocalDateTime fechaConfirmacion;

    public Confirmacion(Usuario usuario) {
        this.usuario = usuario;
        this.confirmado = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public void confirmar() {
        this.confirmado = true;
        this.fechaConfirmacion = LocalDateTime.now();
    }

    public LocalDateTime getFechaConfirmacion() {
        return fechaConfirmacion;
    }

    @Override
    public String toString() {
        return "Confirmacion{" +
                "usuario=" + usuario.getUsername() +
                ", confirmado=" + confirmado +
                '}';
    }
}
