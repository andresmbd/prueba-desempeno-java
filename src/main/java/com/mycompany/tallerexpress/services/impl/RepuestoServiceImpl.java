
package com.mycompany.tallerexpress.services.impl;

import com.mycompany.tallerexpress.exception.ReglaNegocioExeption;
import com.mycompany.tallerexpress.model.Repuesto;
import com.mycompany.tallerexpress.services.RepuestoService;
import java.util.List;
import com.mycompany.tallerexpress.repository.RepuestoRepository;
import com.mycompany.tallerexpress.util.LoggerUtil;
import com.mycompany.tallerexpress.exception.ReglaNegocioExeption;
import com.mycompany.tallerexpress.exception.EntidadNoEncontradaExeption;


public class RepuestoServiceImpl implements RepuestoService{
    
    private final RepuestoRepository repuestoRepository;

    public RepuestoServiceImpl(RepuestoRepository repuestoRepository) {
        this.repuestoRepository = repuestoRepository;
    }
    
    

    @Override
    public Repuesto crearRepuesto(Repuesto repuesto) {
        // codigo unico
        if (repuestoRepository.buscarPorCodigoReferencia(repuesto.getCodigoReferencia()).isPresent()) {
            LoggerUtil.logHttp("POST", "/api/v1/repuestos", 400, "Código de referencia duplicado: " + repuesto.getCodigoReferencia());
            throw new ReglaNegocioExeption("El código de referencia "+repuesto.getCodigoReferencia()+ " ya existe en el sistema.");
        }
        if (repuesto.getStockTotal() < 0 || repuesto.getPrecioUnitario() < 0) {
            LoggerUtil.logHttp("POST", "/api/v1/repuestos", 400, "Valores numéricos inválidos en repuesto");
            throw new ReglaNegocioExeption("El stock y precio unitario deben ser mayores o iguales a 0.");
        }
        if (repuesto.getStockDisponible() == null) {
            repuesto.setStockDisponible(repuesto.getStockTotal());
        }
        Repuesto guardado = repuestoRepository.guardar(repuesto);
        LoggerUtil.logHttp("POST", "/api/v1/repuestos", 201, "Repuesto creado con ID: " + guardado.getId());
        return guardado;
        
    }

    @Override
    public Repuesto actualizarRepuesto(Repuesto repuesto) {
        repuestoRepository.buscarPorId(repuesto.getId())
                .orElseThrow(() -> {
                    LoggerUtil.logHttp("PUT", "/api/v1/repuestos/" + repuesto.getId(), 404, "Repuesto no encontrado");
                    return new EntidadNoEncontradaExeption("No existe repuesto con el ID: " + repuesto.getId());
                });

        repuestoRepository.actualizar(repuesto);
        LoggerUtil.logHttp("PUT", "/api/v1/repuestos/" + repuesto.getId(), 200, "Repuesto actualizado exitosamente");
        return repuesto;
    }

    @Override
    public void cambiarEstadoActivo(Integer id, Boolean isActivo) {
        repuestoRepository.buscarPorId(id)
                .orElseThrow(() -> {
                    LoggerUtil.logHttp("PATCH", "/api/v1/repuestos/" + id + "/estado", 404, "Repuesto no encontrado");
                    return new EntidadNoEncontradaExeption("No existe repuesto con el ID: " + id);
                });

        repuestoRepository.cambiarEstadoActivo(id, isActivo);
        LoggerUtil.logHttp("PATCH", "/api/v1/repuestos/" + id + "/estado", 200, "Estado cambiado a isActivo=" + isActivo);
    }

    @Override
    public Repuesto buscarPorId(Integer id) {
        return repuestoRepository.buscarPorId(id)
                .orElseThrow(() -> {
                    LoggerUtil.logHttp("GET", "/api/v1/repuestos/" + id, 404, "Repuesto no encontrado");
                    return new EntidadNoEncontradaExeption("Repuesto no encontrado con ID: " + id);
                });
    }

    @Override
    public List<Repuesto> listarTodos() {
        LoggerUtil.logHttp("GET", "/api/v1/repuestos", 200, "Listando todos los repuestos");
        return repuestoRepository.listarTodos();
    }

    @Override
    public List<Repuesto> filtrarPorCategoria(String categoria) {
        LoggerUtil.logHttp("GET", "/api/v1/repuestos/categoria?q=" + categoria, 200, "Filtrado por categoría");
        return repuestoRepository.filtrarPorCategoria(categoria);
    }

    @Override
    public List<Repuesto> filtrarPorProveedor(String proveedor) {
        LoggerUtil.logHttp("GET", "/api/v1/repuestos/proveedor?q=" + proveedor, 200, "Filtrado por proveedor");
        return repuestoRepository.filtrarPorProveedor(proveedor);
    }
    
}
