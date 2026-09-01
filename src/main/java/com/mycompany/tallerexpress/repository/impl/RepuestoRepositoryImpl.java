
package com.mycompany.tallerexpress.repository.impl;

import com.mycompany.tallerexpress.conexiondb.ConexionDB;
import com.mycompany.tallerexpress.model.Repuesto;
import com.mycompany.tallerexpress.repository.RepuestoRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class RepuestoRepositoryImpl implements RepuestoRepository{

    @Override
    public Repuesto guardar(Repuesto repuesto) {
        String sql = "INSERT INTO repuestos (codigo_referencia, nombre, categoria, proveedor, stock_total, stock_disponible, precio_unitario, is_activo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id, created_at";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, repuesto.getCodigoReferencia());
            stmt.setString(2, repuesto.getNombre());
            stmt.setString(3, repuesto.getCategoria());
            stmt.setString(4, repuesto.getProveedor());
            stmt.setInt(5, repuesto.getStockTotal());
            stmt.setInt(6, repuesto.getStockDisponible());
            stmt.setDouble(7, repuesto.getPrecioUnitario());
            stmt.setBoolean(8, repuesto.getIsActivo() != null ? repuesto.getIsActivo() : true);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                repuesto.setId(rs.getInt("id"));
                repuesto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            return repuesto;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar repuesto", e);
        }
     }

    @Override
    public Optional<Repuesto> buscarPorId(Integer id) {
        String sql = "SELECT * FROM repuestos WHERE id = ?";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearRepuesto(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar repuesto por ID", e);
        }
        return Optional.empty();
     }

    @Override
    public Optional<Repuesto> buscarPorCodigoReferencia(String codigo) {
        String sql = "SELECT * FROM repuestos WHERE codigo_referencia = ?";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearRepuesto(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar repuesto por codigo de referencia", e);
        }
        return Optional.empty();
     }

    @Override
    public List<Repuesto> listarTodos() {
        List<Repuesto> lista = new ArrayList<>();
        String sql = "SELECT * FROM repuestos";
        try (Connection conn = ConexionDB.conectarDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearRepuesto(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar repuestos", e);
        }
        return lista;
     }

    @Override
    public List<Repuesto> filtrarPorCategoria(String categoria) {
        List<Repuesto> lista = new ArrayList<>();
        String sql = "SELECT * FROM repuestos WHERE ILIKE(categoria, ?)";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + categoria + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearRepuesto(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al filtrar repuestos por categoria", e);
        }
        return lista;
     }

    @Override
    public List<Repuesto> filtrarPorProveedor(String proveedor) {
       List<Repuesto> lista = new ArrayList<>();
        String sql = "SELECT * FROM repuestos WHERE ILIKE(proveedor, ?)";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + proveedor + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearRepuesto(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al filtrar repuestos por proveedor", e);
        }
        return lista; 
     }

    @Override
    public void actualizar(Repuesto repuesto) {
        String sql = "UPDATE repuestos SET nombre = ?, categoria = ?, proveedor = ?, stock_total = ?, stock_disponible = ?, precio_unitario = ?, is_activo = ? WHERE id = ?";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, repuesto.getNombre());
            stmt.setString(2, repuesto.getCategoria());
            stmt.setString(3, repuesto.getProveedor());
            stmt.setInt(4, repuesto.getStockTotal());
            stmt.setInt(5, repuesto.getStockDisponible());
            stmt.setDouble(6, repuesto.getPrecioUnitario());
            stmt.setBoolean(7, repuesto.getIsActivo());
            stmt.setInt(8, repuesto.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar repuesto", e);
        }
     }

    @Override
    public void cambiarEstadoActivo(Integer id, Boolean isActivo) {
        String sql = "UPDATE repuestos SET is_activo = ? WHERE id = ?";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, isActivo);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al cambiar estado activo del repuesto", e);
        }
     }

    @Override
    public void actualizarStockDisponible(Integer id, Integer nuevoStock) {
        String sql = "UPDATE repuestos SET stock_disponible = ? WHERE id = ?";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, nuevoStock);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar stock disponible del repuesto", e);
        }
     }

    private Repuesto mapearRepuesto(ResultSet rs) throws SQLException {
        Repuesto r = new Repuesto();
        r.setId(rs.getInt("id"));
        r.setCodigoReferencia(rs.getString("codigo_referencia"));
        r.setNombre(rs.getString("nombre"));
        r.setCategoria(rs.getString("categoria"));
        r.setProveedor(rs.getString("proveedor"));
        r.setStockTotal(rs.getInt("stock_total"));
        r.setStockDisponible(rs.getInt("stock_disponible"));
        r.setPrecioUnitario(rs.getDouble("precio_unitario"));
        r.setIsActivo(rs.getBoolean("is_activo"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            r.setCreatedAt(ts.toLocalDateTime());
        }
        return r;
    }
    
}
