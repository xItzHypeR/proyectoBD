drop database if exists MedicinaNatural;
create database  MedicinaNatural;
use MedicinaNatural;

create table if not exists Productos (
		idproductos int not null auto_increment primary key,
			nombre varchar(50),
				tipo varchar(45),
					fechaproduccion timestamp,
						fechaexpiracion date,
								precio float

   
);


	create table if not exists Departamentos(
		iddepartamento smallint not null primary key,
			nombre varchar (45),
				descripcion varchar (120)

);
create table if not exists EmpleadoDepartamentos(
		idempleado int not null primary key,
			iddepartamento smallint,

foreign key (iddepartamento) references Departamentos(iddepartamento)

);
create table if  not exists Empleado (
	idempleado int not null

);





create table if not exists produccion (
  idproduccion int primary key,
  idproductos int,
  fechaProduccion date,
  cantidadProducida int



);

