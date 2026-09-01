
package com.mycompany.tallerexpress.repository.impl;

import com.mycompany.tallerexpress.conexiondb.ConexionDB;
import com.mycompany.tallerexpress.model.Cliente;
import com.mycompany.tallerexpress.repository.ClienteRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ClienteRepositoryImpl implements ClienteRepository{

    @Override
    public Cliente guardar(Cliente cliente) {
        String sql = "INSERT INTO clientes (documento, nombre, telefono, email, estado) VALUES (?, ?, ?, ?, ?) RETURNING id, created_at";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getDocumento());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getEstado() != null ? cliente.getEstado() : "ACTIVO");

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cliente.setId(rs.getInt("id"));
                cliente.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            return cliente;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar cliente", e);
        }
     }

    @Override
    public Optional<Cliente> buscarPorId(Integer id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearCliente(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente por ID", e);
        }
        return Optional.empty();
     }

    @Override
    public Optional<Cliente> buscarPorDocumento(String documento) {
        String sql = "SELECT * FROM clientes WHERE documento = ?";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documento);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearCliente(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente por documento", e);
        }
        return Optional.empty();
     }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = ConexionDB.conectarDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearCliente(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar clientes", e);
        }
        return lista;
     }

    @Override
    public void actualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre = ?, telefono = ?, email = ?, estado = ? WHERE id = ?";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getTelefono());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getEstado());
            stmt.setInt(5, cliente.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar cliente", e);
        }
     }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getInt("id"));
        c.setDocumento(rs.getString("documento"));
        c.setNombre(rs.getString("nombre"));
        c.setTelefono(rs.getString("telefono"));
        c.setEmail(rs.getString("email"));
        c.setEstado(rs.getString("estado"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            c.setCreatedAt(ts.toLocalDateTime());
        }
        return c;
    }
    
}
