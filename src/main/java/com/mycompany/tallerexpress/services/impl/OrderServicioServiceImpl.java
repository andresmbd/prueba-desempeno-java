
package com.mycompany.tallerexpress.services.impl;
import com.mycompany.tallerexpress.conexiondb.ConexionDB;
import com.mycompany.tallerexpress.exception.EntidadNoEncontradaExeption;
import com.mycompany.tallerexpress.exception.ReglaNegocioExeption;
import com.mycompany.tallerexpress.model.DetalleOrdenRepuesto;
import com.mycompany.tallerexpress.model.OrdernesDeServicio;
import com.mycompany.tallerexpress.model.Usuario;
import com.mycompany.tallerexpress.repository.OrdenesDeServicioRepository;
import com.mycompany.tallerexpress.repository.RepuestoRepository;
import com.mycompany.tallerexpress.repository.UsuarioRepository;
import com.mycompany.tallerexpress.repository.VehiculoRepository;
import com.mycompany.tallerexpress.services.OrdenServicioService;
import com.mycompany.tallerexpress.util.LoggerUtil;
import java.time.LocalDateTime;
import java.util.List;
import com.mycompany.tallerexpress.model.OrdernesDeServicio;
import com.mycompany.tallerexpress.model.Repuesto;
import com.mycompany.tallerexpress.model.Vehiculo;
import java.sql.Connection;
import java.sql.SQLException;
import com.mycompany.tallerexpress.model.DetalleOrdenRepuesto;

public class OrderServicioServiceImpl implements OrdenServicioService{
    private final OrdenesDeServicioRepository ordenRepository;
    private final VehiculoRepository vehiculoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RepuestoRepository repuestoRepository;

    public OrderServicioServiceImpl(OrdenesDeServicioRepository ordenRepository, VehiculoRepository vehiculoRepository, UsuarioRepository usuarioRepository, RepuestoRepository repuestoRepository) {
        this.ordenRepository = ordenRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.usuarioRepository = usuarioRepository;
        this.repuestoRepository = repuestoRepository;
    }

    @Override
    public OrdernesDeServicio crearOrden(OrdernesDeServicio orden) {
        // Buscamos el vehículo y de una vez sacamos el cliente dueño,
        // así no toca pedirle el ID del cliente aparte al usuario.
        Vehiculo vehiculo = vehiculoRepository.buscarPorId(orden.getVehiculoId())
                .orElseThrow(() -> {
                    LoggerUtil.logHttp("POST", "/api/v1/ordenes", 404, "Vehículo no encontrado");
                    return new EntidadNoEncontradaExeption("No existe el vehículo con ID: " + orden.getVehiculoId());
                });
        orden.setClienteId(vehiculo.getClienteId());

        // El mecánico responsable es obligatorio (la tabla ordenes_de_servicio
        // no permite mecanico_id nulo), por eso lo validamos aquí.
        if (orden.getMecanicoId() == null) {
            throw new ReglaNegocioExeption("Debe indicar el ID del mecánico responsable de la orden.");
        }
        validarMecanico(orden.getMecanicoId());

        // Valores por defecto de la orden. Ojo: el estado inicial debe ser
        // "PENDIENTE" porque así está definido en la base de datos
        // (la tabla solo acepta PENDIENTE, EN_PROCESO, COMPLETADA o CANCELADA).
        orden.setEstado("PENDIENTE");
        orden.setFechaIngreso(LocalDateTime.now());
        if (orden.getCostoTotal() == null) {
            orden.setCostoTotal(0.0);
        }

        try (Connection conn = ConexionDB.conectarDB()) {
        OrdernesDeServicio guardada = ordenRepository.guardar(orden, conn);
        LoggerUtil.logHttp("POST", "/api/v1/ordenes", 201, "Orden creada con ID: " + guardada.getId());
        return guardada;
        } catch (SQLException e) {
            LoggerUtil.logHttp("POST", "/api/v1/ordenes", 500, "Error de base de datos al guardar orden");
            throw new RuntimeException("Error al guardar la orden de servicio en la base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public OrdernesDeServicio cambiarEstado(Integer ordenId, String nuevoEstado) {
        OrdernesDeServicio orden = buscarPorId(ordenId);

        if ("COMPLETADA".equalsIgnoreCase(orden.getEstado()) || "CANCELADA".equalsIgnoreCase(orden.getEstado())) {
            LoggerUtil.logHttp("PATCH", "/api/v1/ordenes/" + ordenId + "/estado", 400, "Orden finalizada previamente");
            throw new ReglaNegocioExeption("No se puede cambiar el estado de una orden que ya está " + orden.getEstado());
        }

        ordenRepository.actualizarEstado(ordenId, nuevoEstado);
        orden.setEstado(nuevoEstado);
        LoggerUtil.logHttp("PATCH", "/api/v1/ordenes/" + ordenId + "/estado", 200, "Estado actualizado a: " + nuevoEstado);
        return orden;
    }

    @Override
    public DetalleOrdenRepuesto agregarDetalle(Integer ordenId, DetalleOrdenRepuesto detalle) {
        OrdernesDeServicio orden = buscarPorId(ordenId);
        if (!"PENDIENTE".equalsIgnoreCase(orden.getEstado()) && !"EN_PROCESO".equalsIgnoreCase(orden.getEstado())) {
            LoggerUtil.logHttp("POST", "/api/v1/ordenes/" + ordenId + "/detalles", 400, "Orden no modificable");
            throw new ReglaNegocioExeption("Solo se pueden agregar repuestos a órdenes en estado PENDIENTE o EN_PROCESO.");
        }

        // 2. Validar existencia y stock del repuesto
        Repuesto repuesto = repuestoRepository.buscarPorId(detalle.getRepuestoId())
                .orElseThrow(() -> {
                    LoggerUtil.logHttp("POST", "/api/v1/ordenes/" + ordenId + "/detalles", 404, "Repuesto no encontrado");
                    return new EntidadNoEncontradaExeption("Repuesto no encontrado con ID: " + detalle.getRepuestoId());
                });

        if (repuesto.getStockDisponible() < detalle.getCantidad()) {
            LoggerUtil.logHttp("POST", "/api/v1/ordenes/" + ordenId + "/detalles", 400, "Stock insuficiente");
            throw new ReglaNegocioExeption("Stock insuficiente para el repuesto '" + repuesto.getNombre() + 
                    "'. Disponible: " + repuesto.getStockDisponible() + ", Solicitado: " + detalle.getCantidad());
        }

        // 3. Preparar detalle y calcular precio
        detalle.setOrdenId(ordenId);
        detalle.setPrecioUnitario(repuesto.getPrecioUnitario());

        // 4. Guardar detalle y descontar stock
        
        try (Connection conn = ConexionDB.conectarDB()) {
        conn.setAutoCommit(false); // Iniciar transacción

            try {
                // Guardar el detalle asociando la conexión activa
                ordenRepository.guardarDetalle(detalle, conn);

                // Calcular y actualizar el nuevo costo total de la orden
                Double subtotalNuevo = detalle.getCantidad() * detalle.getPrecioUnitario();
                Double nuevoCostoTotal = (orden.getCostoTotal() != null ? orden.getCostoTotal() : 0.0) + subtotalNuevo;
                ordenRepository.actualizarCostoTotal(ordenId, nuevoCostoTotal, conn);

                // Descontar stock del repuesto
                repuestoRepository.actualizarStockDisponible(repuesto.getId(), repuesto.getStockDisponible() - detalle.getCantidad());

                conn.commit(); // Confirmar cambios en la BD

                LoggerUtil.logHttp("POST", "/api/v1/ordenes/" + ordenId + "/detalles", 201, "Detalle agregado exitosamente");
                return detalle;

            } catch (Exception e) {
                conn.rollback(); // Deshacer cambios si ocurre cualquier error
                LoggerUtil.logHttp("POST", "/api/v1/ordenes/" + ordenId + "/detalles", 500, "Error en transacción de detalle");
                throw new RuntimeException("Error al procesar el detalle de la orden: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            LoggerUtil.logHttp("POST", "/api/v1/ordenes/" + ordenId + "/detalles", 500, "Error de conexión a la BD");
            throw new RuntimeException("Error de conexión a la base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public OrdernesDeServicio buscarPorId(Integer id) {
        return ordenRepository.buscarPorId(id)
                .orElseThrow(() -> {
                    LoggerUtil.logHttp("GET", "/api/v1/ordenes/" + id, 404, "Orden no encontrada");
                    return new EntidadNoEncontradaExeption("Orden de servicio no encontrada con ID: " + id);
                });
    }

    @Override
    public List<OrdernesDeServicio> listarTodas() {
        LoggerUtil.logHttp("GET", "/api/v1/ordenes", 200, "Listando todas las órdenes");
        return ordenRepository.listarTodas();
    }

    @Override
    public List<OrdernesDeServicio> listarPorVehiculo(Integer vehiculoId) {
        vehiculoRepository.buscarPorId(vehiculoId)
                .orElseThrow(() -> new EntidadNoEncontradaExeption("Vehículo no encontrado con ID: " + vehiculoId));

        LoggerUtil.logHttp("GET", "/api/v1/ordenes/vehiculo/" + vehiculoId, 200, "Consultando historial de servicios por vehículo");
        return ordenRepository.listarPorVehiculo(vehiculoId);
    }

    @Override
    public List<OrdernesDeServicio> listarPorCliente(Integer clienteId){
        LoggerUtil.logHttp("GET", "/api/v1/ordenes/cliente/" + clienteId, 200, "Consultando órdenes por cliente");
        return ordenRepository.listarPorCliente(clienteId);
    }
    
    private void validarMecanico(Integer mecanicoId) {
        // Nota: el sistema solo maneja los roles ADMIN y RECEPCIONISTA
        // (así lo dice la base de datos y el enunciado), no existe un rol
        // "MECANICO" aparte. Por eso aquí solo se valida que el usuario
        // exista y que esté ACTIVO, sin importar su rol.
        Usuario usuario = usuarioRepository.buscarPorId(mecanicoId)
                .orElseThrow(() -> new EntidadNoEncontradaExeption("No existe un usuario con ID: " + mecanicoId + " para asignarlo como mecánico responsable."));

        if (!"ACTIVO".equalsIgnoreCase(usuario.getEstado())) {
            throw new ReglaNegocioExeption("El usuario seleccionado (ID: " + mecanicoId + ") está INACTIVO y no puede ser asignado como mecánico responsable.");
        }
    }
    
    
}
