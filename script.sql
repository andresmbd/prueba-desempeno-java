CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    role VARCHAR(100) NOT NULL DEFAULT 'RECEPCIONISTA' CHECK (role IN ('ADMIN', 'RECEPCIONISTA')),
    estado VARCHAR(100) NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    documento VARCHAR(100) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(100),
    email VARCHAR(100),
    estado VARCHAR(20) DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE vehiculos (
    id SERIAL PRIMARY KEY,
    placa VARCHAR(50) NOT NULL UNIQUE,
    marca VARCHAR(100) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    anio INT NOT NULL CHECK (anio > 1900),
    cliente_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	
    CONSTRAINT fk_vehiculo_cliente FOREIGN KEY (cliente_id) 
    REFERENCES clientes(id) ON DELETE RESTRICT ON UPDATE CASCADE
);


CREATE TABLE repuestos (
    id SERIAL PRIMARY KEY,
    codigo_referencia VARCHAR(100) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    proveedor VARCHAR(100) NOT NULL,
    stock_total INT NOT NULL CHECK (stock_total >= 0),
    stock_disponible INT NOT NULL CHECK (stock_disponible >= 0 AND stock_disponible <= stock_total),
    precio_unitario NUMERIC(12, 2) NOT NULL CHECK (precio_unitario >= 0),
    is_activo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ordenes_de_servicio (
    id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL,
    vehiculo_id INT NOT NULL,
    mecanico_id INT NOT NULL, -- Usuario asignado a la orden
    fecha_ingreso TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    descripcion_problema TEXT NOT NULL,
    diagnostico TEXT,
    costo_total NUMERIC(12, 2) DEFAULT 0.00 CHECK (costo_total >= 0),
    estado VARCHAR(20) DEFAULT 'PENDIENTE' CHECK (estado IN ('PENDIENTE', 'EN_PROCESO', 'COMPLETADA', 'CANCELADA')),

	
    CONSTRAINT fk_orden_cliente FOREIGN KEY (cliente_id) 
        REFERENCES clientes(id) ON DELETE RESTRICT,
		
    CONSTRAINT fk_orden_vehiculo FOREIGN KEY (vehiculo_id) 
        REFERENCES vehiculos(id) ON DELETE RESTRICT,
		
    CONSTRAINT fk_orden_mecanico FOREIGN KEY (mecanico_id) 
        REFERENCES usuarios(id) ON DELETE RESTRICT
);



-- tabla pivote, Relación N:M para repuestos utilizados (ÓRDENES Y REPUESTOS)
CREATE TABLE detalle_orden_repuestos (
    id SERIAL PRIMARY KEY,
    orden_id INT NOT NULL,
    repuesto_id INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(12, 2) NOT NULL CHECK (precio_unitario >= 0),
    subtotal NUMERIC(12, 2) GENERATED ALWAYS AS (cantidad * precio_unitario) STORED,
	
    CONSTRAINT fk_detalle_orden FOREIGN KEY (orden_id) 
        REFERENCES ordenes_de_servicio(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_repuesto FOREIGN KEY (repuesto_id) 
        REFERENCES repuestos(id) ON DELETE RESTRICT
);




