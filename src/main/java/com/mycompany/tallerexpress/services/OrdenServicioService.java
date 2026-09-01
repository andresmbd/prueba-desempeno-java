
package com.mycompany.tallerexpress.services;
import com.mycompany.tallerexpress.model.OrdernesDeServicio;
import com.mycompany.tallerexpress.model.DetalleOrdenRepuesto;
import java.util.List;

public interface OrdenServicioService {
    OrdernesDeServicio crearOrden(OrdernesDeServicio orden);
    OrdernesDeServicio cambiarEstado(Integer ordenId, String nuevoEstado);
    DetalleOrdenRepuesto agregarDetalle(Integer ordenId, DetalleOrdenRepuesto detalle);
    OrdernesDeServicio buscarPorId(Integer id);
    List<OrdernesDeServicio> listarTodas();
    List<OrdernesDeServicio> listarPorVehiculo(Integer vehiculoId);
    List<OrdernesDeServicio> listarPorCliente(Integer clienteId);
}
