
package com.mycompany.tallerexpress.controller;

import com.mycompany.tallerexpress.model.DetalleOrdenRepuesto;
import com.mycompany.tallerexpress.model.OrdernesDeServicio;
import com.mycompany.tallerexpress.services.OrdenServicioService;
import com.mycompany.tallerexpress.util.TablaHelper;

public class OrdenServicioController {
    
    private final OrdenServicioService ordenService;

    public OrdenServicioController(OrdenServicioService ordenService) {
        this.ordenService = ordenService;
    }

    public OrdernesDeServicio crearOrden(OrdernesDeServicio orden) {
        return ordenService.crearOrden(orden);
    }

    public DetalleOrdenRepuesto agregarDetalle(Integer ordenId, DetalleOrdenRepuesto detalle) {
        return ordenService.agregarDetalle(ordenId, detalle);
    }

    public void cambiarEstado(Integer ordenId, String nuevoEstado) {
        ordenService.cambiarEstado(ordenId, nuevoEstado);
    }

    public OrdernesDeServicio buscarPorId(Integer id) {
        return ordenService.buscarPorId(id);
    }

    public String listarTodasTabla() {
        return TablaHelper.formatearOrdenes(ordenService.listarTodas());
    }

    public String listarPorVehiculoTabla(Integer vehiculoId) {
        return TablaHelper.formatearOrdenes(ordenService.listarPorVehiculo(vehiculoId));
    }
}
