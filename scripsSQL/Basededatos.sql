drop database if exists MedicinaNatural;

create database  MedicinaNatural;
use MedicinaNatural;

CREATE TABLE Proveedores (
    idProveedor INT PRIMARY KEY,
    proveedor VARCHAR(255),
    contacto VARCHAR(255),
    direccion VARCHAR(255),
    telefono VARCHAR(255),
    email VARCHAR(255)
);

CREATE TABLE Cliente (
    idCliente INT PRIMARY KEY,
    nombreCliente VARCHAR(255),
    apellidoCliente VARCHAR(255),
    direccion VARCHAR(255),
    telefono VARCHAR(255),
    email VARCHAR(255)
);

CREATE TABLE Compras (
    idCompra INT PRIMARY KEY,
    idProveedor INT,
    fechaCompra DATE,
    totalCompra DECIMAL(10, 2),
    FOREIGN KEY (idProveedor) REFERENCES Proveedores(idProveedor)
);

CREATE TABLE Ingredientes (
    idIngrediente INT PRIMARY KEY,
    ingrediente VARCHAR(255),
    idProveedor INT,
    cantidadDisponible INT,
    costo FLOAT,
    FOREIGN KEY (idProveedor) REFERENCES Proveedores(idProveedor)
);

CREATE TABLE DetalleCompra (
    idDetalleCompra INT PRIMARY KEY,
    idCompra INT,
    idIngrediente INT,
    subtotal DECIMAL(10, 2),
    cantidad INT,
    precioUnitario DECIMAL(10, 2),
    FOREIGN KEY (idCompra) REFERENCES Compras(idCompra),
    FOREIGN KEY (idIngrediente) REFERENCES Ingredientes(idIngrediente)
);



CREATE TABLE IngredientesPrincipales (
    idIngredientePrincipal INT PRIMARY KEY,
    ingredientePrincipal VARCHAR(255)
);

CREATE TABLE Productos (
    idProducto INT PRIMARY KEY,
    nombreProducto VARCHAR(255),
    tipoProducto VARCHAR(255),
    fechaProduccion DATE,
    fechaExpiracion DATE,
    precio FLOAT,
    idIngredientePrincipal INT,
    FOREIGN KEY (idIngredientePrincipal) REFERENCES IngredientesPrincipales(idIngredientePrincipal)
);

CREATE TABLE ProductoIngredientes (
    idProductoIngrediente INT PRIMARY KEY,
    idProducto INT,
    idIngrediente INT,
    cantidad INT,
    FOREIGN KEY (idProducto) REFERENCES Productos(idProducto),
    FOREIGN KEY (idIngrediente) REFERENCES Ingredientes(idIngrediente)
);

CREATE TABLE Inventarios (
    idInventario INT PRIMARY KEY,
    idProducto INT,
    cantidadDisponible INT,
    fechaActualizacion DATE,
    FOREIGN KEY (idProducto) REFERENCES Productos(idProducto)
);

CREATE TABLE Ventas (
    idVenta INT PRIMARY KEY,
    idCliente INT,
    fechaVenta DATE,
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)
);

CREATE TABLE DetalleVenta (
    idDetalleVenta INT PRIMARY KEY,
    idVenta INT,
    idProducto INT,
    cantidad INT,
    precioUnitario DECIMAL(10, 2),
    subtotal DECIMAL(10, 2),
    FOREIGN KEY (idVenta) REFERENCES Ventas(idVenta),
    FOREIGN KEY (idProducto) REFERENCES Productos(idProducto)
);

CREATE TABLE Pedidos (
    idPedidos INT PRIMARY KEY,
    idCliente INT,
    direccion VARCHAR(255),
    nombreCliente VARCHAR(255),
    apellidoCliente VARCHAR(255),
    fechaDeEntrega DATE,
    fechaPedido DATE,
    idDetallePedidos INT,
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)
);

CREATE TABLE DetallePedidos (
    idDetallePedidos INT PRIMARY KEY,
    idPedido INT,
    idProducto INT,
    cantidad INT,
    precioUnitario FLOAT,
    subtotal DECIMAL(10, 2),
    FOREIGN KEY (idPedido) REFERENCES Pedidos(idPedidos),
    FOREIGN KEY (idProducto) REFERENCES Productos(idProducto)
);

CREATE TABLE Facturas (
    idFactura INT PRIMARY KEY,
    idVenta INT,
    idDetallePedidos INT,
    fechaEmision DATE,
    total DECIMAL(10, 2),
    estado VARCHAR(255),
    FOREIGN KEY (idVenta) REFERENCES Ventas(idVenta),
    FOREIGN KEY (idDetallePedidos) REFERENCES DetallePedidos(idDetallePedidos)
);

CREATE TABLE Departamentos (
    idDepartamento INT PRIMARY KEY,
    nombreDepartamento VARCHAR(255),
    descripcion VARCHAR(255)
);

CREATE TABLE Empleados (
    idEmpleado INT auto_increment PRIMARY KEY,
    nombreEmpleado VARCHAR(255),
    apellidoEmpleado VARCHAR(255),
    cargo VARCHAR(255),
    salario DECIMAL(10, 2),
    telefono VARCHAR(255),
    email VARCHAR(255)
);

CREATE TABLE EmpleadoDepartamento (
    idEmpleado INT,
    idDepartamento INT,
    PRIMARY KEY (idEmpleado, idDepartamento),
    FOREIGN KEY (idEmpleado) REFERENCES Empleados(idEmpleado),
    FOREIGN KEY (idDepartamento) REFERENCES Departamentos(idDepartamento)
);

CREATE TABLE Produccion (
    idProduccion INT PRIMARY KEY,
    idProducto INT,
    fechaProduccion DATE,
    cantidadProducida INT,
    idEmpleado INT,
    FOREIGN KEY (idEmpleado) REFERENCES Empleados(idEmpleado)
);

CREATE TABLE TiemposMin (
    idTiempoMin INT PRIMARY KEY,
    tiempoMin TIME
);

CREATE TABLE TiemposMax (
    idTiempoMax INT PRIMARY KEY,
    tiempoMax TIME
);

CREATE TABLE Tiempos (
    idTiempos INT PRIMARY KEY,
    tiempoCronometrado TIME,
    idTiempoMin INT,
    idTiempoMax INT,
    FOREIGN KEY (idTiempoMin) REFERENCES TiemposMin(idTiempoMin),
    FOREIGN KEY (idTiempoMax) REFERENCES TiemposMax(idTiempoMax)
);

CREATE TABLE Observadores (
    idObservador INT PRIMARY KEY,
    nombreObservador VARCHAR(255)
);


CREATE TABLE TiempoActividades (
    idTiempoActividad INT PRIMARY KEY,
    idTiempos INT,
    idEmpleado INT,
    idObservador INT,
    FOREIGN KEY (idTiempos) REFERENCES Tiempos(idTiempos),
    FOREIGN KEY (idEmpleado) REFERENCES Empleados(idEmpleado),
    FOREIGN KEY (idObservador) REFERENCES Observadores(idObservador)
);

alter table TiempoActividades modify column idTiempoActividad INT auto_increment;

INSERT INTO Empleados (nombreEmpleado, apellidoEmpleado, cargo, salario, telefono, email)
VALUES
('María Pérez', 'García', 'Ingeniera de Software', 2500.00, '555-555-5555', 'maria.garcia@email.com'),
('Juan López', 'Martínez', 'Desarrollador Web', 1800.00, '555-555-5556', 'juan.lopez@email.com'),
('Ana González', 'Flores', 'Analista de Datos', 3000.00, '555-555-5557', 'ana.gonzalez@email.com'),
('Pedro Rodríguez', 'Sánchez', 'Diseñador Gráfico', 2200.00, '555-555-5558', 'pedro.rodriguez@email.com'),
('Isabel Jiménez', 'Navarro', 'Especialista en Marketing', 2700.00, '555-555-5559', 'isabel.jimenez@email.com'),
('Carlos Moreno', 'Fernández', 'Administrador de Redes', 2400.00, '555-555-5560', 'carlos.moreno@email.com'),
('Sandra Rubio', 'Alonso', 'Contador', 2100.00, '555-555-5561', 'sandra.rubio@email.com'),
('David Ruiz', 'Gómez', 'Asistente de Recursos Humanos', 1900.00, '555-555-5562', 'david.ruiz@email.com'),
('Laura Gutiérrez', 'Díaz', 'Secretaria', 1700.00, '555-555-5563', 'laura.gutierrez@email.com'),
('Francisco Vázquez', 'Blanco', 'Mensajero', 1600.00, '555-555-5564', 'francisco.vazquez@email.com');


INSERT INTO TiemposMin (tiempoMin) VALUES
  ('01:00'),
  ('05:00'),
  ('03:00'),
  ('05:00'),
  ('04:00'),
  ('05:00'),
  ('03:00'),
  ('05:00'),
  ('01:00'),
  ('14:00');

INSERT INTO Observadores (nombreObservador) VALUES
  ('Juan Pérez'),
  ('Ana López'),
  ('Carlos Gutiérrez'),
  ('María Martínez'),
  ('Pedro Rodríguez'),
  ('Isabel Fernández'),
  ('Diego Sánchez'),
  ('Laura Gómez'),
  ('Alejandro García'),
  ('Patricia Jiménez');


INSERT INTO TiemposMax (tiempoMax) VALUES
  ('10:00'),
  ('09:00'),
  ('30:00'),
  ('16:00'),
  ('17:00'),
  ('19:00'),
  ('30:00'),
  ('18:00'),
  ('30:00'),
  ('16:00');


INSERT INTO Tiempos (tiempoCronometrado, idTiempoMin, idTiempoMax) VALUES
  ('00:45:00', 2, 5),
  ('01:10:00', 3, 6),
  ('01:25:00', 4, 7),
  ('01:55:00', 5, 8),
  ('02:00:00', 6, 9),
  ('02:25:00', 7, 10),
  ('02:45:00', 1, 1),
  ('03:10:00', 2, 2),
  ('03:35:00', 3, 3),
  ('03:50:00', 4, 4);


INSERT INTO TiempoActividades (idTiempos, idEmpleado, idObservador) VALUES
  (1, 2, 1),
  (2, 3, 2),
  (3, 4, 1),
  (4, 5, 2),
  (5, 3, 4);



