
package com.mycompany.tallerexpress.services.impl;

import com.mycompany.tallerexpress.exception.EntidadNoEncontradaExeption;
import com.mycompany.tallerexpress.exception.ReglaNegocioExeption;
import com.mycompany.tallerexpress.model.Usuario;
import com.mycompany.tallerexpress.repository.UsuarioRepository;
import com.mycompany.tallerexpress.services.UsuarioService;
import com.mycompany.tallerexpress.util.LoggerUtil;
import java.util.List;
import com.mycompany.tallerexpress.exception.AuntencacionException;
import javax.naming.AuthenticationException;


public class UsuarioServiceImpl implements UsuarioService{
    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    

    @Override
    public Usuario registrarUsuario(Usuario usuario) {
       if (usuarioRepository.buscarPorUsername(usuario.getUsername()).isPresent()) {
            LoggerUtil.logHttp("POST", "/api/v1/usuarios", 400, "Username duplicado: " + usuario.getUsername());
            throw new ReglaNegocioExeption("El nombre de usuario '" + usuario.getUsername() + "' ya está registrado.");
        }

        // Asignar estado por defecto si no se especifica
        if (usuario.getEstado() == null || usuario.getEstado().isBlank()) {
            usuario.setEstado("ACTIVO");
        }

        Usuario guardado = usuarioRepository.guardar(usuario);
        LoggerUtil.logHttp("POST", "/api/v1/usuarios", 201, "Usuario registrado con ID: " + guardado.getId());
        return guardado; 
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        usuarioRepository.buscarPorId(usuario.getId())
                .orElseThrow(() -> {
                    LoggerUtil.logHttp("PUT", "/api/v1/usuarios/" + usuario.getId(), 404, "Usuario no encontrado");
                    return new EntidadNoEncontradaExeption("Usuario no encontrado con ID: " + usuario.getId());
                });

        usuarioRepository.actualizar(usuario);
        LoggerUtil.logHttp("PUT", "/api/v1/usuarios/" + usuario.getId(), 200, "Usuario actualizado exitosamente");
        return usuario;
    }

    @Override
    public Usuario login(String username, String password) {
        Usuario usuario = usuarioRepository.buscarPorUsername(username)
                .orElseThrow(() -> {
                    LoggerUtil.logHttp("POST", "/api/v1/auth/login", 401, "Intento de login con usuario inexistente: " + username);
                    return new AuntencacionException("Credenciales inválidas.");
                });

        // 2. Validar contraseña
        if (!usuario.getPassword().equals(password)) {
            LoggerUtil.logHttp("POST", "/api/v1/auth/login", 401, "Contraseña incorrecta para usuario: " + username);
            throw new AuntencacionException("Credenciales inválidas.");
        }

        // 3. Validar estado del usuario
        if (!"ACTIVO".equalsIgnoreCase(usuario.getEstado())) {
            LoggerUtil.logHttp("POST", "/api/v1/auth/login", 403, "Intento de ingreso con usuario INACTIVO: " + username);
            throw new AuntencacionException("El usuario se encuentra inactivo. Contacte al administrador.");
        }

        LoggerUtil.logHttp("POST", "/api/v1/auth/login", 200, "Login exitoso para usuario: " + username);
        return usuario;
    }

    @Override
    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> {
                    LoggerUtil.logHttp("GET", "/api/v1/usuarios/" + id, 404, "Usuario no encontrado");
                    return new EntidadNoEncontradaExeption("Usuario no encontrado con ID: " + id);
                });
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.buscarPorUsername(username)
                .orElseThrow(() -> {
                    LoggerUtil.logHttp("GET", "/api/v1/usuarios/username/" + username, 404, "Usuario no encontrado");
                    return new EntidadNoEncontradaExeption("Usuario no encontrado con username: " + username);
                });
    }

    @Override
    public List<Usuario> listarTodos() {
        LoggerUtil.logHttp("GET", "/api/v1/usuarios", 200, "Listando todos los usuarios");
        return usuarioRepository.listarTodos();
    }
    
}
