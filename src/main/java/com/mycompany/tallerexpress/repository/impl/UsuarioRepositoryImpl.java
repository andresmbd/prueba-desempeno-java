
package com.mycompany.tallerexpress.repository.impl;

import com.mycompany.tallerexpress.conexiondb.ConexionDB;
import com.mycompany.tallerexpress.model.Usuario;
import com.mycompany.tallerexpress.repository.UsuarioRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class UsuarioRepositoryImpl implements UsuarioRepository{

    @Override
    public Usuario guardar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, password, nombre, role, estado) VALUES (?, ?, ?, ?, ?) RETURNING id, created_at";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPassword());
            stmt.setString(3, usuario.getNombre());
            stmt.setString(4, usuario.getRole());
            stmt.setString(5, usuario.getEstado());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                usuario.setId(rs.getInt("id"));
                usuario.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            return usuario;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el usuario en la BD", e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(Integer id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por ID", e);
        }
        return Optional.empty();
     }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por username", e);
        }
        return Optional.empty();
     }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection conn = ConexionDB.conectarDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios", e);
        }
        return lista;
     }

    @Override
    public void actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, password = ?, role = ? WHERE id = ?";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getPassword());
            stmt.setString(3, usuario.getRole());
            stmt.setInt(4, usuario.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario", e);
        }
     }

    @Override
    public void cambiarEstado(Integer id, String nuevoEstado) {
        String sql = "UPDATE usuarios SET estado = ? WHERE id = ?";
        try (Connection conn = ConexionDB.conectarDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al cambiar estado del usuario", e);
        }
     }
    
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setNombre(rs.getString("nombre"));
        u.setRole(rs.getString("role"));
        u.setEstado(rs.getString("estado"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            u.setCreatedAt(ts.toLocalDateTime());
        }
        return u;
    }
    
}
