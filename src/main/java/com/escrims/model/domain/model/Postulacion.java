package com.escrims.model.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Postulacion {
    private Long id;
    private Usuario usuario;
    private Scrim scrim;
    private Rol rolSolicitado;
    private String mensaje;
    private LocalDateTime fechaPostulacion;
    private boolean aceptada;
    private boolean rechazada;

    public Postulacion(Usuario usuario, Scrim scrim, Rol rolSolicitado, String mensaje) {
        this.usuario = usuario;
        this.scrim = scrim;
        this.rolSolicitado = rolSolicitado;
        this.mensaje = mensaje;
        this.fechaPostulacion = LocalDateTime.now();
        this.aceptada = false;
        this.rechazada = false;
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

    public Scrim getScrim() {
        return scrim;
    }

    public void setScrim(Scrim scrim) {
        this.scrim = scrim;
    }

    public Rol getRolSolicitado() {
        return rolSolicitado;
    }

    public void setRolSolicitado(Rol rolSolicitado) {
        this.rolSolicitado = rolSolicitado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaPostulacion() {
        return fechaPostulacion;
    }

    public void setFechaPostulacion(LocalDateTime fechaPostulacion) {
        this.fechaPostulacion = fechaPostulacion;
    }

    public boolean isAceptada() {
        return aceptada;
    }

    public void aceptar() {
        this.aceptada = true;
    }

    public boolean isRechazada() {
        return rechazada;
    }

    public void rechazar() {
        this.rechazada = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Postulacion that = (Postulacion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Postulacion{" +
                "usuario=" + usuario.getUsername() +
                ", rol=" + rolSolicitado +
                ", aceptada=" + aceptada +
                ", rechazada=" + rechazada +
                '}';
    }
}
