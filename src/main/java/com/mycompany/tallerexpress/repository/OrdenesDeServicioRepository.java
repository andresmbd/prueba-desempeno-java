
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.model.DetalleOrdenRepuesto;
import com.mycompany.tallerexpress.model.OrdernesDeServicio;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;


public interface OrdenesDeServicioRepository {
    OrdernesDeServicio guardar(OrdernesDeServicio orden, Connection conn);
    void guardarDetalle(DetalleOrdenRepuesto detalle, Connection conn);
    Optional<OrdernesDeServicio> buscarPorId(Integer id);
    List<OrdernesDeServicio> listarPorVehiculo(Integer vehiculoId);
    List<OrdernesDeServicio> listarPorCliente(Integer clienteId);
    List<OrdernesDeServicio> listarTodas();
    void actualizarEstado(Integer id, String nuevoEstado);
    void actualizarCostoTotal(Integer id, Double nuevoCostoTotal, Connection conn);
}
