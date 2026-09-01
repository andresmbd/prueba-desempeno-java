
package com.mycompany.tallerexpress.services.impl;

import com.mycompany.tallerexpress.exception.EntidadNoEncontradaExeption;
import com.mycompany.tallerexpress.exception.ReglaNegocioExeption;
import com.mycompany.tallerexpress.model.Cliente;
import com.mycompany.tallerexpress.repository.ClienteRepository;
import com.mycompany.tallerexpress.services.ClienteService;
import com.mycompany.tallerexpress.util.LoggerUtil;
import java.util.List;


public class ClienteServiceImpl implements ClienteService{
    
    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
    

    @Override
    public Cliente registrarCliente(Cliente cliente) {
        // validar unicidad de documento
        if (clienteRepository.buscarPorDocumento(cliente.getDocumento()).isPresent()) {
            LoggerUtil.logHttp("POST", "/api/v1/clientes", 400, "Documento duplicado: " + cliente.getDocumento());
            throw new ReglaNegocioExeption("Ya existe un cliente registrado con el documento: " + cliente.getDocumento());
        }
        // asignar estado activo por defecto
        if (cliente.getEstado() == null || cliente.getEstado().isBlank()) {
            cliente.setEstado("ACTIVO");
        }
        
        Cliente guardado = clienteRepository.guardar(cliente);
        LoggerUtil.logHttp("POST", "/api/v1/clientes", 201, "Cliente registrado con ID: " + guardado.getId());
        return guardado;
    }

    @Override
    public Cliente actualizarCliente(Cliente cliente) {
        clienteRepository.buscarPorId(cliente.getId())
                .orElseThrow(() -> {
                    LoggerUtil.logHttp("PUT", "/api/v1/clientes/" + cliente.getId(), 404, "Cliente no encontrado");
                    return new EntidadNoEncontradaExeption("Cliente no encontrado con ID: " + cliente.getId());
                });

        clienteRepository.actualizar(cliente);
        LoggerUtil.logHttp("PUT", "/api/v1/clientes/" + cliente.getId(), 200, "Cliente actualizado exitosamente");
        return cliente;
    }

    @Override
    public Cliente buscarPorId(Integer id) {
        return clienteRepository.buscarPorId(id)
                .orElseThrow(() -> {
                    LoggerUtil.logHttp("GET", "/api/v1/clientes/" + id, 404, "Cliente no encontrado");
                    return new EntidadNoEncontradaExeption("Cliente no encontrado con ID: " + id);
                });
    }

    @Override
    public Cliente buscarPorDocumento(String documento) {
        return clienteRepository.buscarPorDocumento(documento)
                .orElseThrow(() -> {
                    LoggerUtil.logHttp("GET", "/api/v1/clientes/doc/" + documento, 404, "Cliente no encontrado");
                    return new EntidadNoEncontradaExeption("Cliente no encontrado con documento: " + documento);
                });
    }

    @Override
    public List<Cliente> listarTodos() {
        LoggerUtil.logHttp("GET", "/api/v1/clientes", 200, "Listando todos los clientes");
        return clienteRepository.listarTodos();
    }
    
}
