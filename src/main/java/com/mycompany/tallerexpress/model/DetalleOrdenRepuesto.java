
package com.mycompany.tallerexpress.model;


public class DetalleOrdenRepuesto {
    private Integer id;
    private Integer ordenId;
    private Integer repuestoId;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;

    public DetalleOrdenRepuesto() {
    }

    public DetalleOrdenRepuesto(Integer id, Integer ordenId, Integer repuestoId, Integer cantidad, Double precioUnitario, Double subtotal) {
        this.id = id;
        this.ordenId = ordenId;
        this.repuestoId = repuestoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public Integer getId() {
        return id;
    }

    public Integer getOrdenId() {
        return ordenId;
    }

    public Integer getRepuestoId() {
        return repuestoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOrdenId(Integer ordenId) {
        this.ordenId = ordenId;
    }

    public void setRepuestoId(Integer repuestoId) {
        this.repuestoId = repuestoId;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
    
}
