package com.mycompany.tallerexpress;

import com.mycompany.tallerexpress.controller.*;
import com.mycompany.tallerexpress.exception.AuntencacionException;
import com.mycompany.tallerexpress.exception.EntidadNoEncontradaExeption;
import com.mycompany.tallerexpress.exception.ReglaNegocioExeption;
import com.mycompany.tallerexpress.model.*;
import com.mycompany.tallerexpress.repository.*;
import com.mycompany.tallerexpress.repository.impl.*;
import com.mycompany.tallerexpress.services.*;
import com.mycompany.tallerexpress.services.impl.*;

import javax.swing.*;
import java.awt.*;

public class Main {

    private static UsuarioController usuarioController;
    private static RepuestoController repuestoController;
    private static ClienteController clienteController;
    private static VehiculoController vehiculoController;
    private static OrdenServicioController ordenController;

    private static Usuario usuarioLogueado;

    public static void main(String[] args) {
        inicializarCapas();

        boolean salir = false;
        while (!salir) {
            if (usuarioLogueado == null) {
                String[] opciones = {"Iniciar Sesión", "Crear Cuenta", "Salir"};
                int opcion = JOptionPane.showOptionDialog(null,
                        "Bienvenido a TallerExpress\nPor favor inicie sesión o cree una cuenta para continuar.",
                        "TallerExpress - Inicio",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, opciones, opciones[0]);

                switch (opcion) {
                    case 0 -> ejecutarLogin();
                    case 1 -> ejecutarRegistroPublico();
                    default -> salir = true;
                }
            } else {
                mostrarMenuPrincipal();
            }
        }

        JOptionPane.showMessageDialog(null, "¡Gracias por usar TallerExpress!", "Salida", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void inicializarCapas() {
        UsuarioRepository usuarioRepo = new UsuarioRepositoryImpl();
        RepuestoRepository repuestoRepo = new RepuestoRepositoryImpl();
        ClienteRepository clienteRepo = new ClienteRepositoryImpl();
        VehiculoRepository vehiculoRepo = new VehiculoRepositoryImpl();
        OrdenesDeServicioRepository ordenRepo = new OrdenesDeServicioRepositoryImpl();


        UsuarioService usuarioServiceBase = new UsuarioServiceImpl(usuarioRepo);
        UsuarioService usuarioService = new UsuarioServiceDecorator(usuarioServiceBase);
        RepuestoService repuestoService = new RepuestoServiceImpl(repuestoRepo);
        ClienteService clienteService = new ClienteServiceImpl(clienteRepo);
        VehiculoService vehiculoService = new VehiculoServiceImpl(vehiculoRepo, clienteRepo);
        OrdenServicioService ordenService = new OrderServicioServiceImpl(ordenRepo, vehiculoRepo, usuarioRepo, repuestoRepo);

        // Controladores
        usuarioController = new UsuarioController(usuarioService);
        repuestoController = new RepuestoController(repuestoService);
        clienteController = new ClienteController(clienteService);
        vehiculoController = new VehiculoController(vehiculoService);
        ordenController = new OrdenServicioController(ordenService);
    }

    private static void ejecutarLogin() {
        try {
            String username = JOptionPane.showInputDialog(null, "Ingrese Usuario:", "Login", JOptionPane.QUESTION_MESSAGE);
            if (username == null || username.trim().isEmpty()) return;

            JPasswordField pf = new JPasswordField();
            int okC = JOptionPane.showConfirmDialog(null, pf, "Ingrese Contraseña:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (okC != JOptionPane.OK_OPTION) return;

            String password = new String(pf.getPassword());

            usuarioLogueado = usuarioController.login(username, password);
            JOptionPane.showMessageDialog(null, "Bienvenido " + usuarioLogueado.getNombre() + "\nRol: " + usuarioLogueado.getRole(), "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
        } catch (AuntencacionException | ReglaNegocioExeption e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void ejecutarRegistroPublico() {
        try {
            Usuario u = new Usuario();
            String username = JOptionPane.showInputDialog(null, "Ingrese un Nombre de Usuario (Username):", "Registro de Cuenta", JOptionPane.QUESTION_MESSAGE);
            if (username == null || username.trim().isEmpty()) return;
            u.setUsername(username.trim());

            JPasswordField pf = new JPasswordField();
            int okC = JOptionPane.showConfirmDialog(null, pf, "Ingrese una Contraseña:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (okC != JOptionPane.OK_OPTION) return;
            u.setPassword(new String(pf.getPassword()));

            String nombre = JOptionPane.showInputDialog(null, "Ingrese su Nombre Completo:", "Registro de Cuenta", JOptionPane.QUESTION_MESSAGE);
            if (nombre == null || nombre.trim().isEmpty()) return;
            u.setNombre(nombre.trim());

            Usuario creado = usuarioController.registrarUsuario(u);
            JOptionPane.showMessageDialog(null, "¡Cuenta creada exitosamente!\nUsuario: " + creado.getUsername() + "\nRol asignado: " + creado.getRole() + "\nYa puede iniciar sesión.", "Registro Completado", JOptionPane.INFORMATION_MESSAGE);

        } catch (ReglaNegocioExeption e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error en el Registro", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al registrar la cuenta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void mostrarMenuPrincipal() {
        String[] opciones = {"Gestión de Repuestos", "Gestión de Clientes", "Gestión de Vehículos", "Gestión de Órdenes", "Gestión de Usuarios", "Cerrar Sesión"};
        int seleccion = JOptionPane.showOptionDialog(null,
                "Usuario actual: " + usuarioLogueado.getNombre() + " (" + usuarioLogueado.getRole() + ")\nSeleccione una opción:",
                "TallerExpress - Menú Principal",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, opciones, opciones[0]);

        switch (seleccion) {
            case 0 -> menuRepuestos();
            case 1 -> menuClientes();
            case 2 -> menuVehiculos();
            case 3 -> menuOrdenes();
            case 4 -> menuUsuarios();
            case 5 -> {
                usuarioLogueado = null;
                JOptionPane.showMessageDialog(null, "Sesión cerrada correctamente.", "Logout", JOptionPane.INFORMATION_MESSAGE);
            }
            default -> {}
        }
    }


    private static void menuRepuestos() {
        String[] opciones = {"Crear Repuesto", "Actualizar Repuesto", "Cambiar Estado (Activo/Inactivo)", "Listar Todos", "Filtrar por Categoría", "Volver"};
        int opt = JOptionPane.showOptionDialog(null, "Módulo de Repuestos", "Repuestos",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

        try {
            switch (opt) {
                case 0 -> {
                    Repuesto r = new Repuesto();
                    r.setCodigoReferencia(JOptionPane.showInputDialog("Código Referencia:"));
                    r.setNombre(JOptionPane.showInputDialog("Nombre:"));
                    r.setCategoria(JOptionPane.showInputDialog("Categoría:"));
                    r.setProveedor(JOptionPane.showInputDialog("Proveedor:"));
                    r.setStockTotal(Integer.parseInt(JOptionPane.showInputDialog("Stock Inicial:")));
                    r.setPrecioUnitario(Double.parseDouble(JOptionPane.showInputDialog("Precio Unitario:")));

                    repuestoController.crearRepuesto(r);
                    JOptionPane.showMessageDialog(null, "Repuesto registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
                case 1 -> {
                    Integer id = Integer.parseInt(JOptionPane.showInputDialog("ID del Repuesto a actualizar:"));
                    Repuesto r = new Repuesto();
                    r.setId(id);
                    r.setNombre(JOptionPane.showInputDialog("Nuevo Nombre:"));
                    r.setCategoria(JOptionPane.showInputDialog("Nueva Categoría:"));
                    r.setProveedor(JOptionPane.showInputDialog("Nuevo Proveedor:"));
                    r.setStockTotal(Integer.parseInt(JOptionPane.showInputDialog("Nuevo Stock Total:")));
                    r.setPrecioUnitario(Double.parseDouble(JOptionPane.showInputDialog("Nuevo Precio Unitario:")));

                    repuestoController.actualizarRepuesto(r);
                    JOptionPane.showMessageDialog(null, "Repuesto actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
                case 2 -> {
                    Integer id = Integer.parseInt(JOptionPane.showInputDialog("ID del Repuesto:"));
                    int confirm = JOptionPane.showConfirmDialog(null, "¿Desea activar el repuesto? (Cancelar para Inactivar)", "Estado", JOptionPane.YES_NO_OPTION);
                    repuestoController.cambiarEstadoActivo(id, confirm == JOptionPane.YES_OPTION);
                    JOptionPane.showMessageDialog(null, "Estado actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
                case 3 -> mostrarTablaMonospaced(repuestoController.listarTodosTabla(), "Listado de Repuestos");
                case 4 -> {
                    String cat = JOptionPane.showInputDialog("Ingrese la categoría:");
                    mostrarTablaMonospaced(repuestoController.filtrarPorCategoriaTabla(cat), "Repuestos por Categoría");
                }
            }
        } catch (ReglaNegocioExeption | EntidadNoEncontradaExeption e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Advertencia de Negocio", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese números válidos en los campos numéricos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private static void menuClientes() {
        String[] opciones = {"Registrar Cliente", "Actualizar Cliente", "Buscar por Documento", "Listar Todos", "Volver"};
        int opt = JOptionPane.showOptionDialog(null, "Módulo de Clientes", "Clientes",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

        try {
            switch (opt) {
                case 0 -> {
                    Cliente c = new Cliente();
                    c.setDocumento(JOptionPane.showInputDialog("Documento / NIT:"));
                    c.setNombre(JOptionPane.showInputDialog("Nombre:"));
                    c.setEmail(JOptionPane.showInputDialog("Email:"));
                    c.setTelefono(JOptionPane.showInputDialog("Teléfono:"));

                    clienteController.registrarCliente(c);
                    JOptionPane.showMessageDialog(null, "Cliente registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
                case 1 -> {
                    String doc = JOptionPane.showInputDialog("Documento del cliente a actualizar:");
                    Cliente c = clienteController.buscarPorDocumento(doc);

                    c.setNombre(JOptionPane.showInputDialog("Nuevo Nombre:", c.getNombre()));
                    c.setEmail(JOptionPane.showInputDialog("Nuevo Email:", c.getEmail()));
                    c.setTelefono(JOptionPane.showInputDialog("Nuevo Teléfono:", c.getTelefono()));

                    clienteController.actualizarCliente(c);
                    JOptionPane.showMessageDialog(null, "Cliente actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
                case 2 -> {
                    String doc = JOptionPane.showInputDialog("Ingrese Documento:");
                    Cliente c = clienteController.buscarPorDocumento(doc);
                    JOptionPane.showMessageDialog(null, "Cliente: " + c.getNombre() + " " + "\nEstado: " + c.getEstado() + "\nEmail: " + c.getEmail(), "Detalle Cliente", JOptionPane.INFORMATION_MESSAGE);
                }
                case 3 -> mostrarTablaMonospaced(clienteController.listarClientesTabla(), "Listado de Clientes");
            }
        } catch (ReglaNegocioExeption | EntidadNoEncontradaExeption e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private static void menuVehiculos() {
        String[] opciones = {"Registrar Vehículo", "Listar por Cliente ID", "Listar Todos", "Volver"};
        int opt = JOptionPane.showOptionDialog(null, "Módulo de Vehículos", "Vehículos",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

        try {
            switch (opt) {
                case 0 -> {
                    Vehiculo v = new Vehiculo();
                    v.setPlaca(JOptionPane.showInputDialog("Placa:"));
                    v.setMarca(JOptionPane.showInputDialog("Marca:"));
                    v.setModelo(JOptionPane.showInputDialog("Modelo:"));
                    v.setAnio(Integer.parseInt(JOptionPane.showInputDialog("Año:")));
                    v.setClienteId(Integer.parseInt(JOptionPane.showInputDialog("ID del Cliente Propietario:")));

                    vehiculoController.registrarVehiculo(v);
                    JOptionPane.showMessageDialog(null, "Vehículo registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
                case 1 -> {
                    Integer id = Integer.parseInt(JOptionPane.showInputDialog("ID del Cliente:"));
                    mostrarTablaMonospaced(vehiculoController.listarPorClienteTabla(id), "Vehículos del Cliente " + id);
                }
                case 2 -> mostrarTablaMonospaced(vehiculoController.listarTodosTabla(), "Listado de Vehículos");
            }
        } catch (ReglaNegocioExeption | EntidadNoEncontradaExeption e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Dato numérico inválido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private static void menuOrdenes() {
        String[] opciones = {"Crear Órden", "Agregar Repuesto a Órden", "Cambiar Estado", "Listar Todas", "Listar por Vehículo", "Volver"};
        int opt = JOptionPane.showOptionDialog(null, "Módulo de Órdenes de Servicio", "Órdenes",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

        try {
            switch (opt) {
                case 0 -> {
                    OrdernesDeServicio o = new OrdernesDeServicio();
                    o.setVehiculoId(Integer.parseInt(JOptionPane.showInputDialog("ID del Vehículo:")));
                    // El cliente de la orden se toma automáticamente del vehículo,

                    String mecStr = JOptionPane.showInputDialog("ID del Mecánico responsable (usuario ya registrado en el sistema):");
                    if (mecStr == null || mecStr.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Debe indicar el mecánico responsable de la orden.", "Dato requerido", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    o.setMecanicoId(Integer.parseInt(mecStr.trim()));

                    o.setDescripcionProblema(JOptionPane.showInputDialog("Descripción de la falla:"));

                    ordenController.crearOrden(o);
                    JOptionPane.showMessageDialog(null, "Órden de Servicio creada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
                case 1 -> {
                    Integer ordenId = Integer.parseInt(JOptionPane.showInputDialog("ID de la Órden de Servicio:"));
                    DetalleOrdenRepuesto detalle = new DetalleOrdenRepuesto();
                    detalle.setRepuestoId(Integer.parseInt(JOptionPane.showInputDialog("ID del Repuesto:")));
                    detalle.setCantidad(Integer.parseInt(JOptionPane.showInputDialog("Cantidad:")));

                    ordenController.agregarDetalle(ordenId, detalle);
                    JOptionPane.showMessageDialog(null, "Repuesto agregado a la órden y stock descontado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
                case 2 -> {
                    Integer ordenId = Integer.parseInt(JOptionPane.showInputDialog("ID de la Órden:"));
                    String[] estados = {"EN_PROCESO", "COMPLETADA", "CANCELADA"};
                    String nuevoEstado = (String) JOptionPane.showInputDialog(null, "Seleccione nuevo estado:",
                            "Cambiar Estado", JOptionPane.QUESTION_MESSAGE, null, estados, estados[0]);

                    if (nuevoEstado != null) {
                        ordenController.cambiarEstado(ordenId, nuevoEstado);
                        JOptionPane.showMessageDialog(null, "Estado actualizado a " + nuevoEstado, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                case 3 -> mostrarTablaMonospaced(ordenController.listarTodasTabla(), "Listado de Órdenes");
                case 4 -> {
                    Integer vId = Integer.parseInt(JOptionPane.showInputDialog("ID del Vehículo:"));
                    mostrarTablaMonospaced(ordenController.listarPorVehiculoTabla(vId), "Historial de Órdenes por Vehículo");
                }
            }
        } catch (ReglaNegocioExeption | EntidadNoEncontradaExeption e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Advertencia de Negocio", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Dato numérico inválido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void menuUsuarios() {
        if (!"ADMIN".equalsIgnoreCase(usuarioLogueado.getRole())) {
            JOptionPane.showMessageDialog(null, "Acceso denegado: Solo el ADMIN puede gestionar usuarios internamente.", "Error de Permisos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] opciones = {"Registrar Usuario", "Listar Usuarios", "Volver"};
        int opt = JOptionPane.showOptionDialog(null, "Módulo de Usuarios", "Usuarios",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

        try {
            switch (opt) {
                case 0 -> {
                    Usuario u = new Usuario();
                    u.setUsername(JOptionPane.showInputDialog("Username:"));
                    u.setPassword(JOptionPane.showInputDialog("Password:"));
                    u.setNombre(JOptionPane.showInputDialog("Nombre Completo:"));

                    // El sistema solo maneja estos dos roles (así está definido
                    // en la base de datos y en el enunciado del proyecto).
                    String[] roles = {"RECEPCIONISTA", "ADMIN"};
                    String rol = (String) JOptionPane.showInputDialog(null, "Seleccione el Rol:",
                            "Rol Usuario", JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);
                    u.setRole(rol);

                    usuarioController.registrarUsuario(u);
                    JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
                case 1 -> mostrarTablaMonospaced(usuarioController.listarUsuariosTabla(), "Listado de Usuarios");
            }
        } catch (ReglaNegocioExeption | EntidadNoEncontradaExeption e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void mostrarTablaMonospaced(String contenido, String titulo) {
        JTextArea textArea = new JTextArea(contenido);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        JOptionPane.showMessageDialog(null, scrollPane, titulo, JOptionPane.PLAIN_MESSAGE);
    }
}