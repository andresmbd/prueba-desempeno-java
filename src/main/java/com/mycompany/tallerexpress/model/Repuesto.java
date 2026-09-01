
package com.mycompany.tallerexpress.model;

import java.time.LocalDateTime;


public class Repuesto {
    private Integer id;
    private String codigoReferencia;
    private String nombre;
    private String categoria;
    private String proveedor;
    private Integer stockTotal;
    private Integer stockDisponible;
    private Double precioUnitario;
    private Boolean isActivo;
    private LocalDateTime createdAt;

    public Repuesto() {
    }

    public Repuesto(Integer id, String codigoReferencia, String nombre, String categoria, String proveedor, Integer stockTotal, Integer stockDisponible, Double precioUnitario, Boolean isActivo, LocalDateTime createdAt) {
        this.id = id;
        this.codigoReferencia = codigoReferencia;
        this.nombre = nombre;
        this.categoria = categoria;
        this.proveedor = proveedor;
        this.stockTotal = stockTotal;
        this.stockDisponible = stockDisponible;
        this.precioUnitario = precioUnitario;
        this.isActivo = isActivo;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public String getCodigoReferencia() {
        return codigoReferencia;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getProveedor() {
        return proveedor;
    }

    public Integer getStockTotal() {
        return stockTotal;
    }

    public Integer getStockDisponible() {
        return stockDisponible;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public Boolean getIsActivo() {
        return isActivo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCodigoReferencia(String codigoReferencia) {
        this.codigoReferencia = codigoReferencia;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public void setStockTotal(Integer stockTotal) {
        this.stockTotal = stockTotal;
    }

    public void setStockDisponible(Integer stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public void setIsActivo(Boolean isActivo) {
        this.isActivo = isActivo;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Repuesto{" + "id=" + id + ", codigoReferencia=" + codigoReferencia + ", nombre=" + nombre + ", categoria=" + categoria + ", proveedor=" + proveedor + ", stockTotal=" + stockTotal + ", stockDisponible=" + stockDisponible + ", precioUnitario=" + precioUnitario + ", isActivo=" + isActivo + '}';
    }
    
}
