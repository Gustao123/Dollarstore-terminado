	CREATE DATABASE DB_DOLLARSTORE
	ON (NAME = DOLLAR_STORE_data,
	FILENAME='C:\MI BASE DATOS\DB_DOLLARSTORE.mdf',
	SIZE = 4,
	MAXSIZE = 10,
	FILEGROWTH = 1)
	GO

	USE DB_DOLLARSTORE
	GO

	/*****Tablas cliente y CRUD****/

	Create table dbo.cliente
	(
	cliente_id int Primary key identity,
	Nombre varchar (20) not null,
	Apellido varchar (20) not null,
	telefono varchar (9) not null,
	direccion varchar (60)not null
	)
	GO

	 /****Tabla venta y su CRUD *****/

	 Create table dbo.venta
	 (
	 venta_id int primary key identity
	 )
	 GO
	  
	  /****Tabla detalle venta y su CRUD***/

	  Create table dbo.detalle_venta 
	  (
	  detalle_v_id int primary key identity,
	  cantidad int ,
	  PrecioUnitario float not null,
	  descripcion varchar (30)
		)
		GO

		/****Tabla producto y su CRUD***/

		Create table dbo.producto
		(
		producto_id int primary key identity,
		descripcion varchar (30)not null,
		cantidad int not null,
		precio_produc float not null,
		existencia int
		)
		GO

		/****Tabla categoria y CRUD***/


		Create table dbo.categoria
		(
		categoria_id int primary key identity,
		descripcion varchar(30)
		)
		GO

		/****Tabla marca y su CRUD*****/


		Create table dbo.marca
		(
		marca_id int primary key identity,
		nombre_marca varchar(20)
		)
		GO

		/****Tabla detalle compra y Su CRUD ***/
		 
		 Create table dbo.detalle_compra
		 (
		 detalle_c_id int primary key identity,
		 cantidad int not null,
		 precio float not null
		 )
		 GO

		 /****Tabla compra y Su CRUD ***/

		 Create table dbo.compra
		(
		compra_id int primary key identity,
		fecha datetime not null,
		costo_compra float
		)
		GO

		/****Tabla proveedor y Su CRUD***/

		Create table dbo.proveedor
		(
		proveedor_id int primary key identity,
		company varchar(20),
		telefono varchar(9),
		correo_electro varchar(40)
		)
		Go

		
		/***Relaciones ala clave foraneas ****/
		
		/***Relacion de venta con cliente***/

		alter table dbo.venta
		add cliente_id int not null
		GO

		alter table dbo.venta
		add constraint fk_venta_IDcliente
		foreign key (cliente_id)
		references dbo.cliente(cliente_id)
		GO


		/***Relacion de detalle venta con venta****/

		alter table dbo.detalle_venta
		add venta_id int not null
		GO

		alter table dbo.detalle_venta 
		Add constraint fk_detalle_venta_IDventa
		foreign key (venta_id)
		references dbo.venta(venta_id)
		GO

		/*** Relacion de detalle venta con producto***/

		alter table dbo.detalle_venta
		add producto_id int not null
		GO

		alter table dbo.detalle_venta 
		Add constraint fk_detalle_venta_IDproducto
		foreign key (producto_id)
		references dbo.producto(producto_id)
		GO
        
		/*** Relacion de producto con marca ***/

		alter table dbo.producto
		add marca_id int not null
		GO

		alter table dbo.producto
		add constraint fk_producto_IDmarca
		foreign key (marca_id)
		references dbo.marca(marca_id)
		Go


		/*** Relacion de producto con categoria ***/

		alter table dbo.producto
		add categoria_id int not null
		GO

		alter table dbo.producto
		add constraint fk_producto_IDcategoria
		foreign key (categoria_id)
		references dbo.categoria(categoria_id)
		Go



		/*** Relacion de detalle compra con producto ***/

		alter table dbo.detalle_compra
		add producto_id int not null
		GO

		alter table dbo.detalle_compra
		Add constraint fk_detalle_compra_IDproducto
		foreign key (producto_id)
		references dbo.producto(producto_id)
		GO
        

		/*** Relacion de detalle compra con compra ***/

		alter table dbo.detalle_compra
		add compra_id int not null
		GO

		alter table dbo.detalle_compra
		Add constraint fk_detalle_compra_IDcompra
		foreign key (compra_id)
		references dbo.compra(compra_id)
		GO


		/*** Relacion de compra con proveedor ***/

		alter table dbo.compra
		add proveedor_id int not null
		GO

		alter table dbo.compra
		Add constraint fk_dcompra_IDproveedor
		foreign key (proveedor_id)
		references dbo.proveedor(proveedor_id)
		GO

		alter table venta 
		add fecha date 
		go


		/***Consulta sql**/


		Select * from categoria

		select * from marca

		select * from cliente

		select * from proveedor

		select * from producto

		select * from venta

		select * from detalle_venta

		select * from detalle_compra

		select * from compra

		/*** Consultas medias***/

		select Nombre, cliente_id from cliente


		Select Nombre from cliente
		where Nombre='Edgar Jose'

		select producto_id,precio_produc  
		from producto where producto_id=2

		select descripcion, precio_produc from 
		producto where producto_id=2 

		select producto_id,descripcion, nombre_marca, precio_produc from producto
		INNER JOIN marca on producto.producto_id=marca.marca_id
		 ORDER BY precio_produc


		select venta_id, fecha from venta;
		

		select venta_id, producto_id, precioUnitario
		from detalle_venta where venta_id>=5
		

		select compra_id, proveedor_id, fecha from compra

		select * from vm_ventas_mayores_pago 

		select nombre_marca from marca
		
	

		/*** VISTAS SQL ***/

		CREATE VIEW  sd_clientes
		AS 
		SELECT cliente_id, Nombre, Apellido, telefono, direccion 
		FROM cliente;
		go


		CREATE VIEW  cp_venta
		AS 
		SELECT venta_id, fecha, pago, cliente_id 
		FROM venta;


		CREATE VIEW  vm_ventas_mayores_pago
		AS
		SELECT producto_id, descripcion, precio_produc
		FROM producto
		Where precio_produc > 100

		

		CREATE VIEW  nm_venta_menores_pago
		AS 
		SELECT producto_id, descripcion, precio_produc
		FROM producto
		WHERE precio_produc < 100

		CREATE VIEW  Ml_productos AS 
		SELECT producto_id,descripcion, cantidad, precio_produc,marca_id, categoria_id
		FROM producto;


		CREATE VIEW  mn_proveedor
		AS 
		SELECT proveedor_id, company, telefono, correo_electro
		FROM proveedor;

		CREATE VIEW  ps_marca 
		AS 
		SELECT marca_id, nombre_marca 
		FROM marca;

		CREATE VIEW  CP_categoria
		AS 
		SELECT categoria_id, descripcion
		FROM categoria;

		CREATE VIEW  sa_detalles_ventas
		AS
		SELECT detalle_v_id, cantidad, PrecioUnitario, descripcion, venta_id, producto_id
		FROM detalle_venta;
		

		CREATE VIEW  ds_detalles_compra
		AS 
		SELECT detalle_c_id, cantidad, precio, producto_id, compra_id
		FROM detalle_compra
		go

	
		

		/***listadores ***/

    CREATE PROCEDURE  po_listarclientes
	
	
AS
BEGIN
	
	SET NOCOUNT ON;

	SELECT * FROM cliente
END
GO

create procedure listarproveedor

AS
begin 

set nocount on;

select*from proveedor

end 
go

create procedure listarproducto

AS
begin 

set nocount on;

select*from producto

end 
go

create procedure listarmarca

AS
begin 

set nocount on;

select*from marca

end 
go


create procedure listarcategoria

AS
begin 

set nocount on;

select*from categoria

end 
go


/***consultas con el JOIN */

SELECT v.cliente_id,Nombre, telefono,pago 
FROM venta v
INNER JOIN  cliente c
ON v.cliente_id=v.cliente_id


SELECT v.cliente_id,Nombre, telefono
FROM venta v
LEFT JOIN  cliente c
ON v.cliente_id=v.cliente_id


SELECT v.cliente_id,Nombre, telefono
FROM venta v
RIGHT JOIN  cliente c
ON v.cliente_id=v.cliente_id
ORDER BY Nombre


SELECT v.fecha,PrecioUnitario 
FROM detalle_venta dv
INNER JOIN venta v ON v.venta_id=dv.venta_id
ORDER BY v.fecha

SELECT producto_id, descripcion, cantidad, P.marca_id,nombre_marca
FROM marca M
INNER JOIN producto P ON M.marca_id=P.marca_id


SELECT producto_id, p.descripcion, cantidad,existencia, precio_produc, m.nombre_marca, c.descripcion 
FROM producto p
INNER JOIN categoria c ON  c.categoria_id=p.categoria_id
INNER JOIN marca M ON M.marca_id=p.marca_id

SELECT correo_electro, telefono, fecha, precio FROM proveedor p
INNER JOIN compra c ON p.proveedor_id=c.compra_id
INNER JOIN detalle_compra dc ON dc.detalle_c_id=c.compra_id
ORDER BY precio DESC

SELECT fecha, PrecioUnitario, p.cantidad FROM detalle_venta dv
INNER JOIN venta v ON dv.detalle_v_id=v.venta_id
INNER JOIN producto p ON dv.detalle_v_id=p.producto_id
ORDER BY fecha 

SELECT c.compra_id ,fecha, company, dc.cantidad, descripcion 
FROM compra c
INNER JOIN proveedor p ON c.compra_id=p.proveedor_id
INNER JOIN detalle_compra dc ON c.compra_id=dc.detalle_c_id
INNER JOIN producto pro ON dc.detalle_c_id=pro.producto_id
WHERE fecha BETWEEN '2024-05-14' AND '2024-07-17'
ORDER BY fecha 


SELECT v.venta_id, v.fecha, c.Nombre, dv.cantidad, precioUnitario, descripcion
FROM venta v
INNER JOIN cliente c ON v.cliente_id=c.cliente_id
INNER JOIN detalle_venta dv ON dv.venta_id=v.venta_id
INNER JOIN producto p ON dv.producto_id=p.producto_id


	SELECT
			v.fecha,
			c.Nombre,
			dv.cantidad,
			pro.descripcion,
			pro.precio_produc
		FROM
			venta v
			INNER JOIN cliente c ON v.cliente_id = c.cliente_id
			INNER JOIN detalle_venta dv  ON dv.venta_id = v.venta_id
			INNER JOIN producto pro ON dv.producto_id = pro.producto_id
		WHERE
			v.fecha BETWEEN '2024-02-12' AND '2024-06-17'
			GROUP BY
    v.fecha, c.Nombre, dv.cantidad, pro.descripcion, pro.precio_produc
		ORDER BY fecha
   
	




/*subconsulta*/

SELECT Nombre, Apellido from cliente c
WHERE (SELECT COUNT(*) FROM venta v WHERE v.cliente_id=c.cliente_id) > 2


SELECT precio_produc, existencia FROM producto P
WHERE precio_produc < (SELECT AVG (precio_produc)FROM producto)


SELECT precio_produc, existencia FROM producto P
WHERE precio_produc > (SELECT AVG (precio_produc)FROM producto)


	SELECT  Nombre, Apellido,descripcion,precio_produc, dv.cantidad, fecha, v.venta_id
	FROM cliente c
	INNER JOIN venta v ON v.cliente_id=c.cliente_id
	INNER JOIN detalle_venta dv ON dv.venta_id=v.venta_id
	INNER JOIN producto p ON dv.producto_id=p.producto_id
	GROUP BY v.venta_id, c.Nombre, c.Apellido, p.descripcion, dv.cantidad, 
	p.precio_produc, v.fecha, v.venta_id
	HAVING 
	v.venta_id= V.venta_id
	go

/* es una prueba 

CREATE VIEW [dbo] [spp_union] AS
SELECT producto_id, p.descripcion AS cantidad, precio_produc, precio_compra, m.nombre_marca, c.descripcion
FROM producto p
INNER JOIN categoria c ON c.categoria_id = p.categoria_id
INNER JOIN marca m ON m.marca_id = p.marca_id;
GO

CREATE PROCEDURE  añadirproducto

@producto_descripcion varchar(30),
@cantidad int,
@precio_producto float,
@precio_compra float,
@nombre_marca varchar(20),
@descripcion varchar(30)
AS 
BEGIN 


INSERT INTO producto(descripcion,cantidad,precio_produc,existencia,marca_id,categoria_id)
VALUES (@producto_descripcion, @cantidad, @precio_producto, @precio_compra,
(SELECT marca_id FROM marca WHERE nombre_marca=@nombre_marca),
(SELECT categoria_id FROM categoria WHERE descripcion=@descripcion));
END
GO*/


CREATE PROCEDURE reportecompra
  @fecha_inicio DATETIME,
    @fecha_final DATETIME
AS
BEGIN
    SELECT
        c.fecha,
        c.costo_compra,
        p.company,
        dc.cantidad,
        pro.descripcion
    FROM
        compra c
        INNER JOIN proveedor p ON c.proveedor_id = p.proveedor_id
        INNER JOIN detalle_compra dc ON c.compra_id = dc.compra_id
        INNER JOIN producto pro ON dc.producto_id = pro.producto_id
    WHERE
        c.fecha BETWEEN @fecha_inicio AND @fecha_final
    ORDER BY
        c.fecha
		
END;
go


CREATE PROCEDURE reporteventa
  @fecha_inicio DATETIME,
    @fecha_final DATETIME
AS
BEGIN
    SELECT

        v.fecha,
        c.Nombre,
        dv.cantidad,
        pro.descripcion,
		pro.precio_produc
    FROM
        venta v
        INNER JOIN cliente c ON v.cliente_id = c.cliente_id
        INNER JOIN detalle_venta dv  ON dv.venta_id = v.venta_id
        INNER JOIN producto pro ON dv.producto_id = pro.producto_id
    WHERE
        v.fecha BETWEEN @fecha_inicio AND @fecha_final
    ORDER BY
        v.fecha;
END;
go


CREATE PROCEDURE Factura
@venta_id int 
AS 
BEGIN 
	SELECT  Nombre, Apellido,descripcion,PrecioUnitario, dv.cantidad, fecha, v.venta_id
	FROM cliente c
	INNER JOIN venta v ON v.cliente_id=c.cliente_id
	INNER JOIN detalle_venta dv ON dv.venta_id=v.venta_id
	INNER JOIN producto p ON dv.producto_id=p.producto_id
	GROUP BY v.venta_id, c.Nombre, c.Apellido, p.descripcion, dv.cantidad, 
	dv.PrecioUnitario, v.fecha, v.venta_id
	HAVING 
	v.venta_id= @venta_id
	end
	Go




---procedimiento buscar----


CREATE PROCEDURE buscarcliente
@parametrobusqueda varchar(25)
AS 
BEGIN 
SELECT * FROM cliente
WHERE Nombre LIKE '%'+RTRIM(@parametrobusqueda)+'%'
OR Apellido LIKE'%'+RTRIM(@parametrobusqueda)+'%'
OR telefono LIKE'%'+RTRIM(@parametrobusqueda)+'%'
OR direccion LIKE '%'+RTRIM(@parametrobusqueda)+'%'
END
GO


CREATE PROCEDURE buscarcategoria
@parametrobusqueda varchar(25)
As
BEGIN 
SELECT * FROM categoria
WHERE categoria_id LIKE '%'+RTRIM(@parametrobusqueda)+'%'
OR descripcion LIKE '%'+RTRIM(@parametrobusqueda)+'%'
END 
GO



CREATE PROCEDURE buscarmarca
@parametrobusqueda varchar(25)
AS 
BEGIN 
SELECT * FROM marca
WHERE marca_id LIKE '%'+RTRIM(@parametrobusqueda)+'%'
OR nombre_marca LIKE '%'+RTRIM(@parametrobusqueda)+'%'
END 
GO




CREATE PROCEDURE buscarproveedor
@parametrobusqueda varchar(25)
AS
BEGIN 
SELECT * FROM proveedor
WHERE company  LIKE '%'+RTRIM(@parametrobusqueda)+'%'
OR correo_electro LIKE'%'+RTRIM(@parametrobusqueda)+'%'
OR telefono LIKE '%'+RTRIM(@parametrobusqueda)+'%'
END
GO

CREATE PROCEDURE buscarproducto
@parametrobusqueda varchar(25)
AS
BEGIN 
SELECT * FROM producto
WHERE descripcion LIKE '%'+RTRIM(@parametrobusqueda)+'%'
OR producto_id LIKE '%'+RTRIM(@parametrobusqueda)+'%'
END
GO

CREATE PROCEDURE buscarventa
@parametrobusqueda datetime
AS
BEGIN
SELECT * FROM venta WHERE fecha LIKE'%'+RTRIM(@parametrobusqueda)+'%'
END
GO


CREATE PROCEDURE buscarDetalleventa
@parametrobusqueda varchar(25)
AS
BEGIN 
SELECT * FROM detalle_venta WHERE descripcion LIKE '%'+RTRIM(@parametrobusqueda)+'%'
END
GO



/***Dato almacenado****/

	create procedure sp_cliente
	@Nombre varchar(20),
	@Apellido varchar(20),
	@telefono varchar(9),
	@direccion varchar(60)
	as 
	begin 
	insert into cliente(Nombre, Apellido, telefono, direccion)
	values ( @Nombre, @Apellido,@telefono, @direccion);
	 end
	 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


	 create procedure sp_leer
	 @cliente_id int 
	 as 
	 begin 
	 select*from cliente where cliente_id = @cliente_id;
	 end	 
	 GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

	 create procedure sp_actualizar_cliente
	 @cliente_id int,
	 @Nombre varchar(20),
	 @Apellido varchar(20),
	 @telefono varchar(9),
	 @direccion varchar(60)
	 as 
	 begin 
	 update cliente
	 set Nombre=@Nombre, Apellido=@Apellido, telefono=@telefono, direccion=@direccion
	 where cliente_id=@cliente_id
	 end
	 	 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
	 create procedure Eliminar_cliente
	 @cliente_id int 
	 as 
	 begin 
	 delete from cliente where cliente_id=@cliente_id;
	 end
	 	 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


 /*** procedimiento venta **/
	 create procedure sp_venta
	 @fecha datetime,
	  @cliente_id int
	 as 
	 begin 
	 insert into venta(fecha,cliente_id)
	 values(@fecha,@cliente_id);
	 end 
	 	 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

	 create procedure sp_leer_venta
	 @venta_id int 
	 as 
	 begin 
	 select*from venta where venta_id=@venta_id;
	 end 
	 	 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

	 create procedure actualizar_venta 
	 @venta_id int,
	 @fecha datetime,
	 @cliente_id int
	 as
	 begin 
	 update venta 
	 set fecha=@fecha, cliente_id=@cliente_id
     where venta_id=@venta_id
	 end 
	 	 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


	/****procedimiento almacenado detalle venta ****/

		create procedure sp_detalle_venta 
		@cantidad int,
		@precioUnitario float,
		@venta_id int,
		@producto_id int 
		as 
		begin 
		insert into detalle_venta(cantidad, precioUnitario,venta_id,producto_id )
		values (@cantidad,@precioUnitario,@venta_id, @producto_id);
		end 
			 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

		create procedure sp_leer_detalle
		@detalle_v_id int
		as 
		begin 
		select*from detalle_venta where detalle_v_id=@detalle_v_id
		end 
			 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

		create procedure actualizar_detalle
		@detalle_v_id int,
		@cantidad int,
		@precioUnitario float,
		@venta_id int,
		@producto_id int
		as 
		begin 
		update detalle_venta
		set cantidad=@cantidad, PrecioUnitario=@precioUnitario, venta_id=@venta_id,
		producto_id=@producto_id
		where detalle_v_id=@detalle_v_id
		end 
			 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO



		/****procedimineto almacenado producto****/

		create  procedure sp_producto
		@descripcion varchar (30),
		@cantidad int, 
		@precio_produc float,
		@existencia int 
		as 
		begin 
		insert into producto(descripcion,cantidad,precio_produc,existencia)
		values (@descripcion,@cantidad,@precio_produc,@existencia);
		end
			 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

		create procedure leer_producto
		@producto_id int
		as 
		begin
		select*from producto where producto_id=@producto_id;
		end
			 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO



		create procedure actualizar_producto
		@producto_id int,
		@descripcion varchar (30),
		@cantidad int, 
		@precio_produc float,
		@existencia int
		as 
		begin 
		update producto
		set descripcion=@descripcion, cantidad=@cantidad,precio_produc=@precio_produc, existencia=@existencia
		where producto_id=@producto_id
		end 
			 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

		create procedure Eliminar_producto
		@producto_id int 
		as
		begin 
		delete from producto where producto_id=@producto_id
		end 
		SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


			/****procedimiento almacenado categoria***/

		create procedure sp_categoria
		@descripcion varchar (30)
		as 
		begin 
		insert into categoria(descripcion)
		values (@descripcion);
		end
			 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

		create procedure leer_categoria
		@categoria_id int 
		as 
		begin 
		select * from categoria where categoria_id=@categoria_id
		end
			 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

		create procedure actualizar_categoria 
		@categoria_id int,
		@descripcion varchar (30)
		as 
		begin 
		update categoria
		set descripcion=@descripcion 
		where categoria_id=@categoria_id
		end
			 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO



		/****  procedimiento almacenado marca ***/

		create procedure sp_marca 
		@nombre_marca varchar(30)
		as 
		begin 
		insert into marca(nombre_marca)
		values (@nombre_marca);
		end 
			 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

		create procedure leer_marca
		@marca_id int
		as 
		begin 
		select * from marca where marca_id=@marca_id
		end
			 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

		create procedure actualizar_marca 
		@marca_id int,
		@nombre_marca varchar(30)
		as 
		begin 
		update marca 
		set nombre_marca=@nombre_marca
		where marca_id=@marca_id
		end
			 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


 /****procedimiento almacenado detalle compra ***/

		 create procedure sp_detalle_compra 
		 @cantidad int,
		 @precio float
		 as 
		 begin 
		 insert into detalle_compra(cantidad,precio)
		 values(@cantidad,@precio);
		 end
		 	 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

		 create procedure leer_detalle_compra
		 @detalle_c_id int 
		 as 
		 begin
		 select * from detalle_compra where detalle_c_id=@detalle_c_id
		 end 
		 	 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

		 create procedure actualizar_detalle_c
		  @detalle_c_id int,
		 @cantidad int,
		 @precio float
		 as 
		 begin 
		 update detalle_compra
		 set cantidad=@cantidad, precio=@precio
		 where detalle_c_id=@detalle_c_id
		 end
		 	 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


/****procedimiento almacenado compra***/

		create procedure sp_compra 
		@costo_compra float,
		@fecha datetime
		as 
		begin 
		insert into compra(costo_compra,fecha)
		values(@costo_compra, @fecha);
		end 
			 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
		create procedure leer_compra
		@compra_id int 
		as 
		begin 
		select *from compra where compra_id=@compra_id
		end 
			 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

		create procedure actualizar_compra
		@compra_id int,
		@costo_compra float,
		@fecha datetime
		as 
		begin 
		update compra
		set costo_compra=@costo_compra, fecha=@fecha
		where compra_id=@compra_id
		end 
			 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


/**** procedimiento almacenado proveedor***/
		
		create procedure sp_proveedor
		@company varchar(20),
		@telefono varchar(8),
		@correo_electro varchar(40)
		as 
		begin 
		insert into proveedor(company, telefono, correo_electro)
		values(@company, @telefono, @correo_electro);
		end 
			 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

		create procedure leer_proveedor
		@proveedor_id int 
		as 
		begin 
		select *from proveedor where proveedor_id=@proveedor_id
		end 
			 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

		create procedure actualizar_proveedor
		@proveedor_id int,
		@company varchar(20),
		@telefono varchar(8),
		@correo_electro varchar(40)
		as 
		begin 
		update proveedor
		set company=@company, telefono=@telefono correo_electro=@correo_electro
		where proveedor_id=@proveedor_id
		end 
			 	GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

		create procedure Eliminar_proveedor
		@proveedor_id int 
		as 
		begin 
		delete from proveedor where proveedor_id=@proveedor_id
		end 
		GO
		/*Actualizar nueva existencia*/

		CREATE PROCEDURE ActualizarExistenciapro
		@nuevaExistencia int,
		@producto_id int 
		AS
		BEGIN 
		Update producto
		SET existencia=@nuevaExistencia
		where producto_id=@producto_id
		END 
		GO

		/*obtener ultimo venta_id*/

		CREATE PROCEDURE obtenerultimoventaid
		AS
		BEGIN 
		SELECT TOP 1 venta_id
		FROM venta
		ORDER BY venta_id DESC 
		END 
		GO


		CREATE PROCEDURE obtenerultimocompra_id
		AS
		BEGIN 
		SELECT TOP 1 compra_id
		FROM compra
		ORDER BY compra_id DESC 
		END 




		/*Dicionario de datos */

		-- Información de las columnas
SELECT 
    TABLE_NAME AS Tabla,
    COLUMN_NAME AS Columna,
    DATA_TYPE AS Tipo_Dato,
    CHARACTER_MAXIMUM_LENGTH AS Longitud
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME IN ('cliente', 'compra', 'categoria', 'detalle_venta', 'detalle_compra', 'marca', 'producto', 'proveedor','venta')
ORDER BY TABLE_NAME, COLUMN_NAME;
GO

-- Información de las claves primarias
SELECT 
    KCU.TABLE_NAME AS Tabla,
    KCU.COLUMN_NAME AS Columna,
    'PRIMARY KEY' AS Restriccion
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS TC
JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS KCU
ON TC.CONSTRAINT_NAME = KCU.CONSTRAINT_NAME
WHERE TC.CONSTRAINT_TYPE = 'PRIMARY KEY'
AND KCU.TABLE_NAME IN ('cliente', 'compra', 'categoria', 'detalle_venta', 'detalle_compra', 'marca', 'producto', 'proveedor','venta')
ORDER BY KCU.TABLE_NAME, KCU.COLUMN_NAME;
GO

-- Información de las claves foráneas
SELECT 
    KCU.TABLE_NAME AS Tabla,
    KCU.COLUMN_NAME AS Columna,
    'FOREIGN KEY' AS Restriccion,
    KCU2.TABLE_NAME AS Tabla_Referencia,
    KCU2.COLUMN_NAME AS Columna_Referencia
FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS AS RC
JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS KCU
ON RC.CONSTRAINT_NAME = KCU.CONSTRAINT_NAME
JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS KCU2
ON RC.UNIQUE_CONSTRAINT_NAME = KCU2.CONSTRAINT_NAME
WHERE KCU.TABLE_NAME IN ('cliente', 'compra', 'categoria', 'detalle_venta', 'detalle_compra', 'marca', 'producto', 'proveedor','venta')
ORDER BY KCU.TABLE_NAME, KCU.COLUMN_NAME;
GO