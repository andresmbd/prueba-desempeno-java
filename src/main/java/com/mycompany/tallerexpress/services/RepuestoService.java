
package com.mycompany.tallerexpress.services;

import com.mycompany.tallerexpress.model.Repuesto;
import java.util.List;


public interface RepuestoService {
    Repuesto crearRepuesto(Repuesto repuesto);
    Repuesto actualizarRepuesto(Repuesto repuesto);
    void cambiarEstadoActivo(Integer id, Boolean isActivo);
    Repuesto buscarPorId(Integer id);
    List<Repuesto> listarTodos();
    List<Repuesto> filtrarPorCategoria(String categoria);
    List<Repuesto> filtrarPorProveedor(String proveedor);
}
