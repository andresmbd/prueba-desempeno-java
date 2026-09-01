
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.model.Repuesto;
import java.util.List;
import java.util.Optional;


public interface RepuestoRepository {
    Repuesto guardar(Repuesto repuesto);
    Optional<Repuesto> buscarPorId(Integer id);
    Optional<Repuesto> buscarPorCodigoReferencia(String codigo);
    List<Repuesto> listarTodos();
    List<Repuesto> filtrarPorCategoria(String categoria);
    List<Repuesto> filtrarPorProveedor(String proveedor);
    void actualizar(Repuesto repuesto);
    void cambiarEstadoActivo(Integer id, Boolean isActivo);
    void actualizarStockDisponible(Integer id, Integer nuevoStock);
}
