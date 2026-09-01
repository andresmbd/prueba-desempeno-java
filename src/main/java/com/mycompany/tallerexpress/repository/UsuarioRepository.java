
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.model.Usuario;
import java.util.List;
import java.util.Optional;


public interface UsuarioRepository {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorId(Integer id);
    Optional<Usuario> buscarPorUsername(String username);
    List<Usuario> listarTodos();
    void actualizar(Usuario usuario);
    void cambiarEstado(Integer id, String nuevoEstado);
}
