
package com.mycompany.tallerexpress.model;

import java.time.LocalDateTime;


public class Vehiculo {
    private Integer id;
    private String placa;
    private String marca;
    private String modelo;
    private Integer anio;
    private Integer clienteId;
    private LocalDateTime createdAt;

    public Vehiculo() {
    }

    public Vehiculo(Integer id, String placa, String marca, String modelo, Integer anio, Integer clienteId, LocalDateTime createdAt) {
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.clienteId = clienteId;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public Integer getAnio() {
        return anio;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Vehiculo{" + "id=" + id + ", placa=" + placa + ", marca=" + marca + ", modelo=" + modelo + ", anio=" + anio + ", clienteId=" + clienteId + '}';
    }
    
    
}
