
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.model.Cliente;
import java.util.List;
import java.util.Optional;


public interface ClienteRepository {
    Cliente guardar(Cliente cliente);
    Optional<Cliente> buscarPorId(Integer id);
    Optional<Cliente> buscarPorDocumento(String documento);
    List<Cliente> listarTodos();
    void actualizar(Cliente cliente);
}
