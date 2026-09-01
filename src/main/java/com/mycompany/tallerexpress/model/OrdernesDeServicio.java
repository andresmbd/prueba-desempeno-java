
package com.mycompany.tallerexpress.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class OrdernesDeServicio {
    private Integer clienteId;
    private Integer id;
    private Integer vehiculoId;
    private Integer mecanicoId;
    private LocalDateTime fechaIngreso;
    private String descripcionProblema;
    private String diagnostico;
    private Double costoTotal;
    private String estado; // "PENDIENTE", "EN_PROCESO", "COMPLETADA", "CANCELADA"
    private List<DetalleOrdenRepuesto> detalles = new ArrayList<>(); // asociar repuestos en la orden
    
    public OrdernesDeServicio() {
        
    }

    public OrdernesDeServicio(Integer id, Integer clienteId, Integer vehiculoId, Integer mecanicoId, LocalDateTime fechaIngreso, String descripcionProblema, String diagnostico, Double costoTotal, String estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.vehiculoId = vehiculoId;
        this.mecanicoId = mecanicoId;
        this.fechaIngreso = fechaIngreso;
        this.descripcionProblema = descripcionProblema;
        this.diagnostico = diagnostico;
        this.costoTotal = costoTotal;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public Integer getVehiculoId() {
        return vehiculoId;
    }

    public Integer getMecanicoId() {
        return mecanicoId;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public String getDescripcionProblema() {
        return descripcionProblema;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public Double getCostoTotal() {
        return costoTotal;
    }

    public String getEstado() {
        return estado;
    }

    public List<DetalleOrdenRepuesto> getDetalles() {
        return detalles;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public void setVehiculoId(Integer vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public void setMecanicoId(Integer mecanicoId) {
        this.mecanicoId = mecanicoId;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public void setDescripcionProblema(String descripcionProblema) {
        this.descripcionProblema = descripcionProblema;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public void setCostoTotal(Double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setDetalles(List<DetalleOrdenRepuesto> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "OrdernesDeServicio{" + "id=" + id + ", clienteId=" + clienteId + ", vehiculoId=" + vehiculoId + ", fechaIngreso=" + fechaIngreso + ", costoTotal=" + costoTotal + ", estado=" + estado + '}';
    }
    
    
}
