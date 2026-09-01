
package com.mycompany.tallerexpress.services;

import com.mycompany.tallerexpress.model.Vehiculo;
import java.util.List;


public interface VehiculoService {
    Vehiculo registrarVehiculo(Vehiculo vehiculo);
    Vehiculo buscarPorId(Integer id);
    Vehiculo buscarPorPlaca(String placa);
    List<Vehiculo> listarPorCliente(Integer clienteId);
    List<Vehiculo> listarTodos();
}
