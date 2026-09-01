
package com.mycompany.tallerexpress.repository.impl;

import com.mycompany.tallerexpress.conexiondb.ConexionDB;
import com.mycompany.tallerexpress.model.Vehiculo;
import com.mycompany.tallerexpress.repository.VehiculoRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehiculoRepositoryImpl implements VehiculoRepository{

    @Override
    public Vehiculo guardar(Vehiculo vehiculo) {
        String sql = "INSERT INTO vehiculos (placa, marca, modelo, anio, cliente_id) VALUES (?, ?, ?, ?, ?) RETURNING id, created_at";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vehiculo.getPlaca());
            stmt.setString(2, vehiculo.getMarca());
            stmt.setString(3, vehiculo.getModelo());
            stmt.setInt(4, vehiculo.getAnio());
            stmt.setInt(5, vehiculo.getClienteId());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                vehiculo.setId(rs.getInt("id"));
                vehiculo.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            return vehiculo;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar vehiculo", e);
        }
     }

    @Override
    public Optional<Vehiculo> buscarPorId(Integer id) {
        String sql = "SELECT * FROM vehiculos WHERE id = ?";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearVehiculo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar vehiculo por ID", e);
        }
        return Optional.empty();
     }

    @Override
    public Optional<Vehiculo> buscarPorPlaca(String placa) {
        String sql = "SELECT * FROM vehiculos WHERE placa = ?";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, placa);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearVehiculo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar vehiculo por placa", e);
        }
        return Optional.empty();
     }

    @Override
    public List<Vehiculo> listarPorCliente(Integer clienteId) {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM vehiculos WHERE cliente_id = ?";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(mapearVehiculo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar vehiculos por cliente", e);
        }
        return lista;
    }

    @Override
    public List<Vehiculo> listarTodos() {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM vehiculos";
        try (Connection conn = ConexionDB.conectarDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearVehiculo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar todos los vehiculos", e);
        }
        return lista;
    }

    private Vehiculo mapearVehiculo(ResultSet rs) throws SQLException {
        Vehiculo v = new Vehiculo();
        v.setId(rs.getInt("id"));
        v.setPlaca(rs.getString("placa"));
        v.setMarca(rs.getString("marca"));
        v.setModelo(rs.getString("modelo"));
        v.setAnio(rs.getInt("anio"));
        v.setClienteId(rs.getInt("cliente_id"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            v.setCreatedAt(ts.toLocalDateTime());
        }
        return v;
    }
    
}
