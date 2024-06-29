Drop database Medicinanatural_DM;
Create database MedicinaNatural_DM;

use MedicinaNatural_DM;

--------------------------------------------------------------------------
 CREATE TABLE sl_Observador(
 id_observador INT PRIMARY KEY,
 NombreObservador VARCHAR (50)
 
 );
 CREATE TABLE sl_empleados(
 id_empleado INT PRIMARY KEY,
 nombreEmpleado VARCHAR(255),
apellidoEmpleado VARCHAR(255),
cargo VARCHAR(255),
salario DECIMAL(10, 2),
telefono VARCHAR(255),
email VARCHAR(255)
 );
 
 CREATE TABLE dim_control(
 id_control INT PRIMARY KEY,
id_observador INT,
 id_empleado INT,
 
 FOREIGN KEY(id_observador) REFERENCES sl_Observador(id_observador),
 FOREIGN KEY(id_empleado) REFERENCES sl_empleados(id_empleado)
 );
--------------------------------------------------------------------------
CREATE TABLE sl_TiemposMin (
    id_TiempoMin INT PRIMARY KEY,
    tiempoMin TIME
);
CREATE TABLE sl_TiemposMax (
    id_TiempoMax INT PRIMARY KEY,
    tiempoMax TIME
);
CREATE TABLE dim_tiempo(
id_Tiempo INT PRIMARY KEY,
id_TiempoMin INT,
id_TiempoMax INT,

FOREIGN KEY(id_TiempoMax) REFERENCES sl_TiemposMax(id_TiempoMax),
FOREIGN KEY(id_TiempoMin) REFERENCES sl_TiemposMin(id_TiempoMin)
);
--------------------------------------------------------------------------
CREATE TABLE sl_proveedor(
id_proveedor INT PRIMARY KEY,
 proveedor VARCHAR(255),
contacto VARCHAR(255),
direccion VARCHAR(255),
telefono VARCHAR(255),
email VARCHAR(255)
);
CREATE TABLE sl_clientes(
id_cliente INT PRIMARY KEY,
nombreCliente VARCHAR(255),
apellidoCliente VARCHAR(255),
direccion VARCHAR(255),
telefono VARCHAR(255),
email VARCHAR(255)
);
CREATE TABLE dim_compras(
id_compra INT PRIMARY KEY,
 fechaCompra DATE,
 totalCompra DECIMAL,
 id_proveedor INT,
 id_cliente INT,
 
 FOREIGN KEY(id_proveedor) REFERENCES sl_proveedor(id_proveedor),
  FOREIGN KEY(id_cliente) REFERENCES sl_clientes(id_cliente)
);
--------------------------------------------------------------------------
CREATE TABLE sl_Ingredientes(
id_ingrediente INT PRIMARY KEY,
nombreIngrediente VARCHAR (50),
cantidad INT
);

CREATE TABLE sl_Productos(
id_producto INT PRIMARY KEY,
nombreProducto VARCHAR(255),
tipoProducto VARCHAR(255),
fechaProduccion DATE,
fechaExpiracion DATE,
precio FLOAT
);

CREATE TABLE dim_Inventario(
id_inventario INT PRIMARY KEY,
cantidadDisponible INT,
fechaActualizacion DATE,
id_ingrediente INT,
id_producto INT,

FOREIGN KEY(id_ingrediente) REFERENCES sl_Ingredientes(id_ingrediente),
  FOREIGN KEY(id_Producto) REFERENCES sl_Productos(id_Producto)

    
);
--------------------------------------------------------------------------

CREATE TABLE fact_register(
register_id INT PRIMARY KEY,
id_control INT,
id_Tiempo INT,
id_compra INT,
id_inventario INT,

FOREIGN KEY(id_control) REFERENCES dim_control(id_control),
  FOREIGN KEY(id_Tiempo) REFERENCES dim_tiempo(id_Tiempo),
FOREIGN KEY(id_compra) REFERENCES dim_compras(id_compra),
  FOREIGN KEY(id_inventario) REFERENCES dim_Inventario(id_inventario)

);