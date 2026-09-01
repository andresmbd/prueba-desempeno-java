
package com.mycompany.tallerexpress.repository;

import com.mycompany.tallerexpress.model.Vehiculo;
import java.util.List;
import java.util.Optional;


public interface VehiculoRepository {
    Vehiculo guardar(Vehiculo vehiculo);
    Optional<Vehiculo> buscarPorId(Integer id);
    Optional<Vehiculo> buscarPorPlaca(String placa);
    List<Vehiculo> listarPorCliente(Integer clienteId);
    List<Vehiculo> listarTodos();
}
