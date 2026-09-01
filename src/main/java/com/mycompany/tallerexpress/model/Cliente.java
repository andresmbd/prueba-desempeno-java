
package com.mycompany.tallerexpress.model;

import java.time.LocalDateTime;

public class Cliente {
    private Integer id;
    private String documento;
    private String nombre;
    private String telefono;
    private String email;
    private String estado; // "ACTIVO" o "INACTIVO"
    private LocalDateTime createdAt;

    public Cliente() {
    }

    public Cliente(Integer id, String documento, String nombre, String telefono, String email, String estado, LocalDateTime createdAt) {
        this.id = id;
        this.documento = documento;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.estado = estado;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
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

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Cliente{" + "id=" + id + ", documento=" + documento + ", nombre=" + nombre + ", email=" + email + ", estado=" + estado + '}';
    }
    
    
}
