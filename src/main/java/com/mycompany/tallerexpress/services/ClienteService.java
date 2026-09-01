
package com.mycompany.tallerexpress.services;

import com.mycompany.tallerexpress.model.Cliente;
import java.util.List;


public interface ClienteService {
    Cliente registrarCliente(Cliente cliente);
    Cliente actualizarCliente(Cliente cliente);
    Cliente buscarPorId(Integer id);
    Cliente buscarPorDocumento(String documento);
    List<Cliente> listarTodos();
}
