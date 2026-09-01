
package com.mycompany.tallerexpress.model;

import java.time.LocalDateTime;


public class Usuario {
    private Integer id;
    private String username;
    private String password;
    private String nombre;
    private String role; // "ADMIN" o "RECEPCIONISTA"
    private String estado; // "ACTIVO" o "INACTIVO"
    private LocalDateTime createdAt;

    public Usuario() {
    }

    public Usuario(Integer id, String username, String password, String nombre, String role, String estado, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.role = role;
        this.estado = estado;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRole() {
        return role;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", username=" + username + ", nombre=" + nombre + ", role=" + role + ", estado=" + estado + '}';
    }
    
    
}
