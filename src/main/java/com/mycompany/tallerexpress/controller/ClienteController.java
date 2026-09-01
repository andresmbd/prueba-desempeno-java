
package com.mycompany.tallerexpress.controller;

import com.mycompany.tallerexpress.model.Cliente;
import com.mycompany.tallerexpress.services.ClienteService;
import com.mycompany.tallerexpress.util.TablaHelper;

public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public Cliente registrarCliente(Cliente cliente) {
        return clienteService.registrarCliente(cliente);
    }

    public Cliente actualizarCliente(Cliente cliente) {
        return clienteService.actualizarCliente(cliente);
    }

    public Cliente buscarPorDocumento(String documento) {
        return clienteService.buscarPorDocumento(documento);
    }

    public String listarClientesTabla() {
        return TablaHelper.formatearClientes(clienteService.listarTodos());
    }
}
