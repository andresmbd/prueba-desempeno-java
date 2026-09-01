
package com.mycompany.tallerexpress.controller;

import com.mycompany.tallerexpress.model.Repuesto;
import com.mycompany.tallerexpress.services.RepuestoService;
import com.mycompany.tallerexpress.util.TablaHelper;

import java.util.List;
public class RepuestoController {
    private final RepuestoService repuestoService;

    public RepuestoController(RepuestoService repuestoService) {
        this.repuestoService = repuestoService;
    }

    public Repuesto crearRepuesto(Repuesto repuesto) {
        return repuestoService.crearRepuesto(repuesto);
    }

    public Repuesto actualizarRepuesto(Repuesto repuesto) {
        return repuestoService.actualizarRepuesto(repuesto);
    }

    public void cambiarEstadoActivo(Integer id, Boolean isActivo) {
        repuestoService.cambiarEstadoActivo(id, isActivo);
    }

    public String listarTodosTabla() {
        return TablaHelper.formatearRepuestos(repuestoService.listarTodos());
    }

    public String filtrarPorCategoriaTabla(String categoria) {
        return TablaHelper.formatearRepuestos(repuestoService.filtrarPorCategoria(categoria));
    }

    public String filtrarPorProveedorTabla(String proveedor) {
        return TablaHelper.formatearRepuestos(repuestoService.filtrarPorProveedor(proveedor));
    }
}
