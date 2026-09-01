
package com.mycompany.tallerexpress.controller;

import com.mycompany.tallerexpress.model.Usuario;
import com.mycompany.tallerexpress.services.UsuarioService;
import com.mycompany.tallerexpress.util.TablaHelper;
import java.util.List;

public class UsuarioController {
    
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Usuario login(String username, String password) {
        return usuarioService.login(username, password);
    }

    public Usuario registrarUsuario(Usuario usuario) {
        return usuarioService.registrarUsuario(usuario);
    }

    public Usuario actualizarUsuario(Usuario usuario) {
        return usuarioService.actualizarUsuario(usuario);
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioService.buscarPorId(id);
    }

    public String listarUsuariosTabla() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return TablaHelper.formatearUsuarios(usuarios);
    }
}
