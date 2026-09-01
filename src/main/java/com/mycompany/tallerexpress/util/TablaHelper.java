
package com.mycompany.tallerexpress.util;

import com.mycompany.tallerexpress.model.Cliente;
import com.mycompany.tallerexpress.model.OrdernesDeServicio;
import com.mycompany.tallerexpress.model.Repuesto;
import com.mycompany.tallerexpress.model.Usuario;
import com.mycompany.tallerexpress.model.Vehiculo;

import java.util.List;

public class TablaHelper {
    public static String formatearRepuestos(List<Repuesto> repuestos) {
        if (repuestos.isEmpty()) return "No hay repuestos registrados.";
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4s | %-12s | %-20s | %-12s | %-8s | %-8s | %-10s | %-10s%n",
                "ID", "CÓDIGO", "NOMBRE", "CATEGORÍA", "STOCK", "DISP.", "PRECIO", "ESTADO"));
        sb.append("-".repeat(100)).append("\n");

        for (Repuesto r : repuestos) {
            String estado = Boolean.TRUE.equals(r.getIsActivo()) ? "[ACTIVO]" : "[INACTIVO]";
            sb.append(String.format("%-4d | %-12s | %-20s | %-12s | %-8d | %-8d | $%-9.2f | %-10s%n",
                    r.getId(), r.getCodigoReferencia(), cortar(r.getNombre(), 20),
                    cortar(r.getCategoria(), 12), r.getStockTotal(), r.getStockDisponible(),
                    r.getPrecioUnitario(), estado));
        }
        return sb.toString();
    }

    public static String formatearClientes(List<Cliente> clientes) {
        if (clientes.isEmpty()) return "No hay clientes registrados.";
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4s | %-12s | %-25s | %-12s | %-10s%n",
                "ID", "DOCUMENTO", "NOMBRE COMPLETO", "TELÉFONO", "ESTADO"));
        sb.append("-".repeat(70)).append("\n");

        for (Cliente c : clientes) {
            sb.append(String.format("%-4d | %-12s | %-25s | %-12s | %-10s%n",
                    c.getId(), c.getDocumento(), c.getNombre() + " ",
                    c.getTelefono() != null ? c.getTelefono() : "N/A", c.getEstado()));
        }
        return sb.toString();
    }

    public static String formatearVehiculos(List<Vehiculo> vehiculos) {
        if (vehiculos.isEmpty()) return "No hay vehículos registrados.";
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4s | %-10s | %-15s | %-15s | %-6s | %-10s%n",
                "ID", "PLACA", "MARCA", "MODELO", "AÑO", "CLIENTE ID"));
        sb.append("-".repeat(70)).append("\n");

        for (Vehiculo v : vehiculos) {
            sb.append(String.format("%-4d | %-10s | %-15s | %-15s | %-6d | %-10d%n",
                    v.getId(), v.getPlaca(), cortar(v.getMarca(), 15),
                    cortar(v.getModelo(), 15), v.getAnio(), v.getClienteId()));
        }
        return sb.toString();
    }

    public static String formatearUsuarios(List<Usuario> usuarios) {
        if (usuarios.isEmpty()) return "No hay usuarios registrados.";
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4s | %-15s | %-25s | %-15s | %-10s%n",
                "ID", "USERNAME", "NOMBRE", "ROL", "ESTADO"));
        sb.append("-".repeat(75)).append("\n");

        for (Usuario u : usuarios) {
            sb.append(String.format("%-4d | %-15s | %-25s | %-15s | %-10s%n",
                    u.getId(), u.getUsername(), cortar(u.getNombre(), 25),
                    u.getRole(), u.getEstado()));
        }
        return sb.toString();
    }

    public static String formatearOrdenes(List<OrdernesDeServicio> ordenes) {
        if (ordenes.isEmpty()) return "No hay órdenes de servicio registradas.";
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4s | %-10s | %-12s | %-12s | %-12s | %-12s%n",
                "ID", "VEHÍCULO", "MECÁNICO", "ESTADO", "COSTO TOTAL", "FECHA INGRESO"));
        sb.append("-".repeat(75)).append("\n");

        for (OrdernesDeServicio o : ordenes) {
            sb.append(String.format("%-4d | %-10d | %-12s | %-12s | $%-11.2f | %-12s%n",
                    o.getId(), o.getVehiculoId(),
                    o.getMecanicoId() != null ? String.valueOf(o.getMecanicoId()) : "SIN ASIGNAR",
                    o.getEstado(), o.getCostoTotal() != null ? o.getCostoTotal() : 0.0,
                    o.getFechaIngreso() != null ? o.getFechaIngreso().toLocalDate().toString() : "N/A"));
        }
        return sb.toString();
    }

    private static String cortar(String texto, int max) {
        if (texto == null) return "";
        return texto.length() > max ? texto.substring(0, max - 3) + "..." : texto;
    }

}
