
package com.mycompany.tallerexpress.services.impl;

import com.mycompany.tallerexpress.model.Usuario;
import com.mycompany.tallerexpress.services.UsuarioService;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Decorador de UsuarioService.
 *
 * Lo que hace es "envolver" al UsuarioServiceImpl real y, justo antes de
 * registrar un usuario, le rellena las propiedades por defecto que pide
 * el enunciado (role: RECEPCIONISTA, estado: ACTIVO, createdAt: ahora)
 * SOLO si esos datos vienen vacíos. Así no se toca ni se duplica la
 * lógica que ya tiene UsuarioServiceImpl, solo se le agrega este paso
 * antes de llamarlo.
 *
 * Los demás métodos simplemente delegan al servicio original, tal cual.
 */
public class UsuarioServiceDecorator implements UsuarioService {

    private final UsuarioService usuarioServiceOriginal;

    public UsuarioServiceDecorator(UsuarioService usuarioServiceOriginal) {
        this.usuarioServiceOriginal = usuarioServiceOriginal;
    }

    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        // Propiedades por defecto antes de crear el usuario
        if (usuario.getRole() == null || usuario.getRole().isBlank()) {
            usuario.setRole("RECEPCIONISTA");
        }
        if (usuario.getEstado() == null || usuario.getEstado().isBlank()) {
            usuario.setEstado("ACTIVO");
        }
        usuario.setCreatedAt(LocalDateTime.now());

        // Se llama al método create original, sin modificarlo
        return usuarioServiceOriginal.registrarUsuario(usuario);
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        return usuarioServiceOriginal.actualizarUsuario(usuario);
    }

    @Override
    public Usuario login(String username, String password) {
        return usuarioServiceOriginal.login(username, password);
    }

    @Override
    public Usuario buscarPorId(Integer id) {
        return usuarioServiceOriginal.buscarPorId(id);
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        return usuarioServiceOriginal.buscarPorUsername(username);
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioServiceOriginal.listarTodos();
    }
}
