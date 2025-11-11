package com.escrims.model.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Usuario {
    private Long id;
    private String username;
    private String password;
    private String email;
    private LocalDateTime fechaRegistro;
    private List<Estadistica> estadisticas;
    private List<Rol> rolesPreferidos;

    public Usuario(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fechaRegistro = LocalDateTime.now();
        this.estadisticas = new ArrayList<>();
        this.rolesPreferidos = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public List<Estadistica> getEstadisticas() {
        return new ArrayList<>(estadisticas);
    }

    public void agregarEstadistica(Estadistica estadistica) {
        this.estadisticas.add(estadistica);
    }

    public Estadistica getEstadisticaPorJuego(Juego juego) {
        return estadisticas.stream()
                .filter(e -> e.getJuego() == juego)
                .findFirst()
                .orElse(null);
    }

    public List<Rol> getRolesPreferidos() {
        return new ArrayList<>(rolesPreferidos);
    }

    public void setRolesPreferidos(List<Rol> rolesPreferidos) {
        this.rolesPreferidos = new ArrayList<>(rolesPreferidos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
