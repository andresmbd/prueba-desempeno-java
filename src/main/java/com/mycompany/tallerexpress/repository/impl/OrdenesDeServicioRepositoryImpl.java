
package com.mycompany.tallerexpress.repository.impl;

import com.mycompany.tallerexpress.conexiondb.ConexionDB;
import com.mycompany.tallerexpress.model.DetalleOrdenRepuesto;
import com.mycompany.tallerexpress.model.OrdernesDeServicio;
import com.mycompany.tallerexpress.repository.OrdenesDeServicioRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class OrdenesDeServicioRepositoryImpl implements OrdenesDeServicioRepository{

    @Override
    public OrdernesDeServicio guardar(OrdernesDeServicio orden, Connection conn) {
        String sql = "INSERT INTO ordenes_de_servicio (cliente_id, vehiculo_id, mecanico_id, descripcion_problema, diagnostico, costo_total, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id, fecha_ingreso";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orden.getClienteId());
            stmt.setInt(2, orden.getVehiculoId());
            stmt.setInt(3, orden.getMecanicoId());
            stmt.setString(4, orden.getDescripcionProblema());
            stmt.setString(5, orden.getDiagnostico());
            stmt.setDouble(6, orden.getCostoTotal() != null ? orden.getCostoTotal() : 0.0);
            stmt.setString(7, orden.getEstado() != null ? orden.getEstado() : "PENDIENTE");

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                orden.setId(rs.getInt("id"));
                orden.setFechaIngreso(rs.getTimestamp("fecha_ingreso").toLocalDateTime());
            }
            return orden;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la orden de servicio en la transacción", e);
        }
     }

    @Override
    public void guardarDetalle(DetalleOrdenRepuesto detalle, Connection conn) {
        String sql = "INSERT INTO detalle_orden_repuestos (orden_id, repuesto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getOrdenId());
            stmt.setInt(2, detalle.getRepuestoId());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setDouble(4, detalle.getPrecioUnitario());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el detalle de la orden en la transacción", e);
        }
     }

    @Override
    public Optional<OrdernesDeServicio> buscarPorId(Integer id) {
        String sql = "SELECT * FROM ordenes_de_servicio WHERE id = ?";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                OrdernesDeServicio orden = mapearOrden(rs);
                orden.setDetalles(obtenerDetallesPorOrdenId(orden.getId(), conn));
                return Optional.of(orden);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar orden por ID", e);
        }
        return Optional.empty();
     }

    @Override
    public List<OrdernesDeServicio> listarPorVehiculo(Integer vehiculoId) {
        List<OrdernesDeServicio> lista = new ArrayList<>();
        String sql = "SELECT * FROM ordenes_de_servicio WHERE vehiculo_id = ? ORDER BY fecha_ingreso DESC";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehiculoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrdernesDeServicio orden = mapearOrden(rs);
                orden.setDetalles(obtenerDetallesPorOrdenId(orden.getId(), conn));
                lista.add(orden);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar ordenes por vehiculo", e);
        }
        return lista;
    }

    @Override
    public List<OrdernesDeServicio> listarPorCliente(Integer clienteId) {
        List<OrdernesDeServicio> lista = new ArrayList<>();
        String sql = "SELECT * FROM ordenes_de_servicio WHERE cliente_id = ? ORDER BY fecha_ingreso DESC";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrdernesDeServicio orden = mapearOrden(rs);
                orden.setDetalles(obtenerDetallesPorOrdenId(orden.getId(), conn));
                lista.add(orden);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar ordenes por cliente", e);
        }
        return lista;
     }

    @Override
    public List<OrdernesDeServicio> listarTodas() {
        List<OrdernesDeServicio> lista = new ArrayList<>();
        String sql = "SELECT * FROM ordenes_de_servicio ORDER BY id DESC";
        try (Connection conn = ConexionDB.conectarDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                OrdernesDeServicio orden = mapearOrden(rs);
                lista.add(orden);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todas las ordenes", e);
        }
        return lista;
    }

    @Override
    public void actualizarEstado(Integer id, String nuevoEstado) {
        String sql = "UPDATE ordenes_de_servicio SET estado = ? WHERE id = ?";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estado de la orden", e);
        }
    }

    @Override
    public void actualizarCostoTotal(Integer id, Double nuevoCostoTotal, Connection conn) {
        String sql = "UPDATE ordenes_de_servicio SET costo_total = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, nuevoCostoTotal);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar costo total de la orden", e);
        }
    }

    private List<DetalleOrdenRepuesto> obtenerDetallesPorOrdenId(Integer ordenId, Connection conn) throws SQLException {
        List<DetalleOrdenRepuesto> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_orden_repuestos WHERE orden_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ordenId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DetalleOrdenRepuesto d = new DetalleOrdenRepuesto();
                d.setId(rs.getInt("id"));
                d.setOrdenId(rs.getInt("orden_id"));
                d.setRepuestoId(rs.getInt("repuesto_id"));
                d.setCantidad(rs.getInt("cantidad"));
                d.setPrecioUnitario(rs.getDouble("precio_unitario"));
                d.setSubtotal(rs.getDouble("subtotal"));
                detalles.add(d);
            }
        }
        return detalles;
    }

    private OrdernesDeServicio mapearOrden(ResultSet rs) throws SQLException {
        OrdernesDeServicio o = new OrdernesDeServicio();
        o.setId(rs.getInt("id"));
        o.setClienteId(rs.getInt("cliente_id"));
        o.setVehiculoId(rs.getInt("vehiculo_id"));
        o.setMecanicoId(rs.getInt("mecanico_id"));
        o.setDescripcionProblema(rs.getString("descripcion_problema"));
        o.setDiagnostico(rs.getString("diagnostico"));
        o.setCostoTotal(rs.getDouble("costo_total"));
        o.setEstado(rs.getString("estado"));
        Timestamp ts = rs.getTimestamp("fecha_ingreso");
        if (ts != null) {
            o.setFechaIngreso(ts.toLocalDateTime());
        }
        return o;
    }

    
}
