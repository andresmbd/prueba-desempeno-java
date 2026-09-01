
package com.mycompany.tallerexpress.services;

import com.mycompany.tallerexpress.model.Usuario;
import java.util.List;


public interface UsuarioService {
    Usuario registrarUsuario(Usuario usuario);
    Usuario actualizarUsuario(Usuario usuario);
    Usuario login(String username, String password);
    Usuario buscarPorId(Integer id);
    Usuario buscarPorUsername(String username);
    List<Usuario> listarTodos();
}
