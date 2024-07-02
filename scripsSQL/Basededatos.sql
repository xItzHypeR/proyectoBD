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
    idProducto INT auto_increment PRIMARY KEY,
    nombreProducto VARCHAR(255),
    tipoProducto VARCHAR(255),
    fechaProduccion DATE,
    fechaExpiracion DATE,
    precio FLOAT,
    idIngredientePrincipal INT,
    FOREIGN KEY (idIngredientePrincipal) REFERENCES IngredientesPrincipales(idIngredientePrincipal)
);

DELIMITER $$

CREATE TRIGGER SetFechaExpiracion
BEFORE INSERT ON Productos
FOR EACH ROW
BEGIN
    IF NEW.fechaExpiracion IS NULL THEN
        SET NEW.fechaExpiracion = DATE_ADD(NEW.fechaProduccion, INTERVAL 3 YEAR);
    END IF;
END$$

DELIMITER ;

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
    idProduccion INT auto_increment PRIMARY KEY,
    idProducto INT,
    fechaProduccion DATE,
    cantidadProducida INT,
    idEmpleado INT,
    FOREIGN KEY (idEmpleado) REFERENCES Empleados(idEmpleado)
);
/*
CREATE TABLE TiemposMin (
    idTiempoMin INT auto_increment PRIMARY KEY,
    tiempoMin TIME
);

CREATE TABLE TiemposMax (
    idTiempoMax INT auto_increment PRIMARY KEY,
    tiempoMax TIME
);

CREATE TABLE Tiempos (
    idTiempos INT PRIMARY KEY,
	idEmpleado INT,
    tiempoMin TIME,
    tiempoMax TIME,
    foreign key (idEmpleado) REFERENCES empleados(idEmpleado),
    FOREIGN KEY (tiempoMin) REFERENCES TiemposMin(tiempoMin),
    FOREIGN KEY (tiempoMax) REFERENCES TiemposMax(tiempoMax)
);
*/

CREATE TABLE Observadores (
    idObservador INT PRIMARY KEY,
    nombreObservador VARCHAR(255)
);

/*
CREATE TABLE TiempoActividades (
    idTiempoActividad INT auto_increment PRIMARY KEY,
    idTiempos INT,
    idEmpleado INT,
    idObservador INT,
    FOREIGN KEY (idTiempos) REFERENCES Tiempos(idTiempos),
    FOREIGN KEY (idEmpleado) REFERENCES Empleados(idEmpleado),
    FOREIGN KEY (idObservador) REFERENCES Observadores(idObservador)
);
*/


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

/*
INSERT INTO TiemposMin (tiempoMin)
VALUES
  ('00:00:00'),
  ('01:00:00'),
  ('02:00:00'),
  ('03:00:00'),
  ('04:00:00'),
  ('05:00:00'),
  ('06:00:00'),
  ('07:00:00'),
  ('08:00:00'),
  ('09:00:00');

INSERT INTO TiemposMax (tiempoMax)
VALUES
  ('09:00:00'),
  ('10:00:00'),
  ('11:00:00'),
  ('12:00:00'),
  ('13:00:00'),
  ('14:00:00'),
  ('15:00:00'),
  ('16:00:00'),
  ('17:00:00'),
  ('18:00:00');
  */
  
INSERT INTO IngredientesPrincipales (idIngredientePrincipal, ingredientePrincipal)
VALUES
  (1, 'Romero y Miel'),
  (2, 'Aloe Vera'),
  (3, 'Propóleo'),
  (4, 'Lavanda'),
  (5, 'Hierbas Relajantes'),
  (6, 'Eucalipto'),
  (7, 'Ortiga y Manzanilla'),
  (8, 'Hamamelis'),
  (9, 'Árnica'),
  (10, 'Menta');

  
INSERT INTO Productos (nombreProducto, tipoProducto, fechaProduccion, fechaExpiracion, precio, idIngredientePrincipal)
VALUES
  ('Shampoo de Romero y Miel', 'Medicina natural', '2024-06-25', '2025-06-24', 45.00, 1),
  ('Crema facial de Aloe Vera', 'Medicina natural', '2024-06-22', '2025-06-21', 30.00, 2),
  ('Jarabe para la tos con Propóleo', 'Medicina natural', '2024-06-20', '2024-12-20', 25.00, 3),
  ('Jabón artesanal de lavanda', 'Medicina natural', '2024-06-27', '2025-06-26', 23.00, 4),
  ('Infusión de hierbas relajantes', 'Medicina natural', '2024-06-24', '2024-12-24', 20.00, 5),
  ('Aceite esencial de eucalipto', 'Medicina natural', '2024-06-28', '2025-06-27', 14.00, 6),
  ('Shampoo de Ortiga y Manzanilla', 'Medicina natural', '2024-06-21', '2025-06-20', 12.00, 7),
  ('Tónico facial de hamamelis', 'Medicina natural', '2024-06-26', '2025-06-25', 11.00, 8),
  ('Pomada calmante de árnica', 'Medicina natural', '2024-06-23', '2024-12-23', 10.00, 9),
  ('Dentífrico natural con menta', 'Medicina natural', '2024-06-25', '2025-06-24', 7.00, 10);


INSERT INTO Produccion (idProducto, fechaProduccion, cantidadProducida, idEmpleado)
VALUES
  (1, '2024-06-20', 100, 1),
  (2, '2024-06-21', 50, 3),
  (3, '2024-06-22', 75, 4),
  (1, '2024-06-23', 120, 10),
  (2, '2024-06-24', 65, 6),
  (3, '2024-06-25', 80, 7),
  (1, '2024-06-26', 150, 9),
  (2, '2024-06-27', 90, 2),
  (3, '2024-06-28', 105, 5),
  (1, '2024-06-29', 130, 8);



