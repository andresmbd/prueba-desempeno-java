
package com.mycompany.tallerexpress.services.impl;

import com.mycompany.tallerexpress.exception.EntidadNoEncontradaExeption;
import com.mycompany.tallerexpress.exception.ReglaNegocioExeption;
import com.mycompany.tallerexpress.model.Cliente;
import com.mycompany.tallerexpress.model.Vehiculo;
import com.mycompany.tallerexpress.repository.ClienteRepository;
import com.mycompany.tallerexpress.repository.VehiculoRepository;
import com.mycompany.tallerexpress.services.VehiculoService;
import com.mycompany.tallerexpress.util.LoggerUtil;
import java.util.List;


public class VehiculoServiceImpl implements VehiculoService{
    
    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;

    public VehiculoServiceImpl(VehiculoRepository vehiculoRepository, ClienteRepository clienteRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.clienteRepository = clienteRepository;
    }
    

    @Override
    public Vehiculo registrarVehiculo(Vehiculo vehiculo) {
        // Validar que el cliente exista y esté ACTIVO
        Cliente cliente = clienteRepository.buscarPorId(vehiculo.getClienteId())
                .orElseThrow(() -> {
                    LoggerUtil.logHttp("POST", "/api/v1/vehiculos", 404, "Cliente no encontrado para el vehículo");
                    return new EntidadNoEncontradaExeption("No se puede registrar el vehículo. No existe cliente con ID: " + vehiculo.getClienteId());
                });

        if (!"ACTIVO".equalsIgnoreCase(cliente.getEstado())) {
            LoggerUtil.logHttp("POST", "/api/v1/vehiculos", 400, "Cliente INACTIVO");
            throw new ReglaNegocioExeption("No se puede asociar un vehículo a un cliente inactivo.");
        }

        // Validar que la placa sea única
        if (vehiculoRepository.buscarPorPlaca(vehiculo.getPlaca()).isPresent()) {
            LoggerUtil.logHttp("POST", "/api/v1/vehiculos", 400, "Placa duplicada: " + vehiculo.getPlaca());
            throw new ReglaNegocioExeption("Ya existe un vehículo registrado con la placa: " + vehiculo.getPlaca());
        }

        Vehiculo guardado = vehiculoRepository.guardar(vehiculo);
        LoggerUtil.logHttp("POST", "/api/v1/vehiculos", 201, "Vehículo registrado con ID: " + guardado.getId());
        return guardado;
    }

    @Override
    public Vehiculo buscarPorId(Integer id) {
        return vehiculoRepository.buscarPorId(id)
                .orElseThrow(() -> {
                    LoggerUtil.logHttp("GET", "/api/v1/vehiculos/" + id, 404, "Vehículo no encontrado");
                    return new EntidadNoEncontradaExeption("Vehículo no encontrado con ID: " + id);
                });
    }

    @Override
    public Vehiculo buscarPorPlaca(String placa) {
        return vehiculoRepository.buscarPorPlaca(placa)
                .orElseThrow(() -> {
                    LoggerUtil.logHttp("GET", "/api/v1/vehiculos/placa/" + placa, 404, "Vehículo no encontrado");
                    return new EntidadNoEncontradaExeption("Vehículo no encontrado con placa: " + placa);
                });
    }

    @Override
    public List<Vehiculo> listarPorCliente(Integer clienteId) {
        // existencia del cliente
        clienteRepository.buscarPorId(clienteId)
                .orElseThrow(() -> new EntidadNoEncontradaExeption("Cliente no encontrado con ID: " + clienteId));

        LoggerUtil.logHttp("GET", "/api/v1/vehiculos/cliente/" + clienteId, 200, "Listando vehículos del cliente");
        return vehiculoRepository.listarPorCliente(clienteId);
    }

    @Override
    public List<Vehiculo> listarTodos() {
        LoggerUtil.logHttp("GET", "/api/v1/vehiculos", 200, "Listando todos los vehículos");
        return vehiculoRepository.listarTodos();
    }
    
}
