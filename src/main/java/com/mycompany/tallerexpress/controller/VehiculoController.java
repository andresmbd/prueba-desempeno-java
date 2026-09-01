
package com.mycompany.tallerexpress.controller;

import com.mycompany.tallerexpress.model.Vehiculo;
import com.mycompany.tallerexpress.services.VehiculoService;
import com.mycompany.tallerexpress.util.TablaHelper;

public class VehiculoController {
    
    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    public Vehiculo registrarVehiculo(Vehiculo vehiculo) {
        return vehiculoService.registrarVehiculo(vehiculo);
    }

    public String listarPorClienteTabla(Integer clienteId) {
        return TablaHelper.formatearVehiculos(vehiculoService.listarPorCliente(clienteId));
    }

    public String listarTodosTabla() {
        return TablaHelper.formatearVehiculos(vehiculoService.listarTodos());
    }
}
