DROP DATABASE IF EXISTS Cajero_Super;
CREATE DATABASE Cajero_Super
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_0900_ai_ci;

USE Cajero_Super;

CREATE TABLE producto (
  id_producto INT AUTO_INCREMENT PRIMARY KEY,
  codigo_barra VARCHAR(50) NOT NULL UNIQUE,
  nombre VARCHAR(120) NOT NULL,
  precio DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
  stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
  activo TINYINT(1) NOT NULL DEFAULT 1,
  creado_en DATETIME DEFAULT CURRENT_TIMESTAMP,
  actualizado_en DATETIME NULL ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_producto_nombre ON producto(nombre);

CREATE TABLE boleta (
  id_boleta INT AUTO_INCREMENT PRIMARY KEY,
  folio VARCHAR(30) NOT NULL UNIQUE,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

  subtotal DECIMAL(10,2) NOT NULL DEFAULT 0,
  descuento DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (descuento >= 0),
  iva DECIMAL(10,2) NOT NULL DEFAULT 0,
  total DECIMAL(10,2) NOT NULL DEFAULT 0,

  metodo_pago ENUM('EFECTIVO','DEBITO','CREDITO','TRANSFERENCIA') NOT NULL,
  monto_pagado DECIMAL(10,2) NOT NULL DEFAULT 0,
  vuelto DECIMAL(10,2) NOT NULL DEFAULT 0,

  estado ENUM('EMITIDA','ANULADA') NOT NULL DEFAULT 'EMITIDA'
);

CREATE INDEX idx_boleta_fecha ON boleta(fecha);

CREATE TABLE boleta_detalle (
  id_detalle INT AUTO_INCREMENT PRIMARY KEY,
  id_boleta INT NOT NULL,
  id_producto INT NOT NULL,
  cantidad INT NOT NULL CHECK (cantidad > 0),
  precio_unitario DECIMAL(10,2) NOT NULL CHECK (precio_unitario >= 0),
  subtotal_linea DECIMAL(10,2) NOT NULL,

  FOREIGN KEY (id_boleta) REFERENCES boleta(id_boleta) ON DELETE CASCADE,
  FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

CREATE TRIGGER bi_boleta_detalle_calc
BEFORE INSERT ON boleta_detalle
FOR EACH ROW
SET NEW.subtotal_linea = NEW.cantidad * NEW.precio_unitario;

DELIMITER $$

CREATE TRIGGER ai_boleta_detalle_stock
AFTER INSERT ON boleta_detalle
FOR EACH ROW
BEGIN
  IF (SELECT stock FROM producto WHERE id_producto = NEW.id_producto) < NEW.cantidad THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Stock insuficiente';
  END IF;

  UPDATE producto
  SET stock = stock - NEW.cantidad
  WHERE id_producto = NEW.id_producto;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER ai_boleta_recalculo
AFTER INSERT ON boleta_detalle
FOR EACH ROW
BEGIN
  UPDATE boleta
  SET subtotal = (
        SELECT IFNULL(SUM(subtotal_linea),0)
        FROM boleta_detalle
        WHERE id_boleta = NEW.id_boleta
      ),
      iva = ROUND((
        SELECT IFNULL(SUM(subtotal_linea),0)
        FROM boleta_detalle
        WHERE id_boleta = NEW.id_boleta
      ) * 0.19,2),
      total = ROUND((
        SELECT IFNULL(SUM(subtotal_linea),0)
        FROM boleta_detalle
        WHERE id_boleta = NEW.id_boleta
      ) * 1.19,2)
  WHERE id_boleta = NEW.id_boleta;
END$$

DELIMITER ;

CREATE VIEW vw_boleta_resumen AS
SELECT id_boleta, folio, fecha, subtotal, descuento, iva, total, metodo_pago, monto_pagado, vuelto
FROM boleta;

/*  ============================================================================================  */

DROP DATABASE IF EXISTS Cajero_Super;
CREATE DATABASE Cajero_Super
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_0900_ai_ci;

USE Cajero_Super;

CREATE TABLE producto (
  id_producto INT AUTO_INCREMENT PRIMARY KEY,
  codigo_barra VARCHAR(50) NOT NULL UNIQUE,
  nombre VARCHAR(120) NOT NULL,
  precio DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
  stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
  activo TINYINT(1) NOT NULL DEFAULT 1,
  creado_en DATETIME DEFAULT CURRENT_TIMESTAMP,
  actualizado_en DATETIME NULL ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_producto_nombre ON producto(nombre);

CREATE TABLE boleta (
  id_boleta INT AUTO_INCREMENT PRIMARY KEY,
  folio VARCHAR(30) NOT NULL UNIQUE,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

  subtotal DECIMAL(10,2) NOT NULL DEFAULT 0,
  descuento DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (descuento >= 0),
  iva DECIMAL(10,2) NOT NULL DEFAULT 0,
  total DECIMAL(10,2) NOT NULL DEFAULT 0,

  metodo_pago ENUM('EFECTIVO','DEBITO','CREDITO','TRANSFERENCIA') NOT NULL,
  monto_pagado DECIMAL(10,2) NOT NULL DEFAULT 0,
  vuelto DECIMAL(10,2) NOT NULL DEFAULT 0,

  estado ENUM('EMITIDA','ANULADA') NOT NULL DEFAULT 'EMITIDA'
);

CREATE INDEX idx_boleta_fecha ON boleta(fecha);

CREATE TABLE boleta_detalle (
  id_detalle INT AUTO_INCREMENT PRIMARY KEY,
  id_boleta INT NOT NULL,
  id_producto INT NOT NULL,
  cantidad INT NOT NULL CHECK (cantidad > 0),
  precio_unitario DECIMAL(10,2) NOT NULL CHECK (precio_unitario >= 0),
  subtotal_linea DECIMAL(10,2) NOT NULL,

  FOREIGN KEY (id_boleta) REFERENCES boleta(id_boleta) ON DELETE CASCADE,
  FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

SHOW TABLES;

INSERT INTO producto (codigo_barra, nombre, precio, stock) VALUES
('780000000001', 'Pan', 1200.00, 10),
('780000000002', 'Leche 1L', 1100.00, 5),
('780000000003', 'Arroz 1Kg', 1600.00, 3);

SELECT * FROM producto;

DROP TRIGGER IF EXISTS bi_boleta_detalle_calc;

CREATE TRIGGER bi_boleta_detalle_calc
BEFORE INSERT ON boleta_detalle
FOR EACH ROW
SET NEW.subtotal_linea = NEW.cantidad * NEW.precio_unitario;

SHOW TRIGGERS;

DROP TRIGGER IF EXISTS ai_boleta_detalle_stock;

DELIMITER $$

CREATE TRIGGER ai_boleta_detalle_stock
AFTER INSERT ON boleta_detalle
FOR EACH ROW
BEGIN
  IF (SELECT stock FROM producto WHERE id_producto = NEW.id_producto) < NEW.cantidad THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Stock insuficiente';
  END IF;

  UPDATE producto
  SET stock = stock - NEW.cantidad
  WHERE id_producto = NEW.id_producto;
END$$

DELIMITER ;

DROP TRIGGER IF EXISTS ai_boleta_recalculo;

DELIMITER $$

CREATE TRIGGER ai_boleta_recalculo
AFTER INSERT ON boleta_detalle
FOR EACH ROW
BEGIN
  UPDATE boleta
  SET subtotal = (
        SELECT IFNULL(SUM(subtotal_linea),0)
        FROM boleta_detalle
        WHERE id_boleta = NEW.id_boleta
      ),
      iva = ROUND((
        SELECT IFNULL(SUM(subtotal_linea),0)
        FROM boleta_detalle
        WHERE id_boleta = NEW.id_boleta
      ) * 0.19,2),
      total = ROUND((
        SELECT IFNULL(SUM(subtotal_linea),0)
        FROM boleta_detalle
        WHERE id_boleta = NEW.id_boleta
      ) * 1.19,2)
  WHERE id_boleta = NEW.id_boleta;
END$$

DELIMITER ;

CREATE OR REPLACE VIEW vw_boleta_resumen AS
SELECT id_boleta, folio, fecha, subtotal, descuento, iva, total, metodo_pago, monto_pagado, vuelto
FROM boleta;

INSERT INTO boleta (folio, metodo_pago, monto_pagado, descuento)
VALUES ('BOL-000001', 'EFECTIVO', 5000, 0);

SELECT * FROM boleta;

INSERT INTO boleta_detalle (id_boleta, id_producto, cantidad, precio_unitario, subtotal_linea)
VALUES (1, 1, 2, 1200, 0);

SELECT * FROM boleta_detalle;
SELECT id_producto, stock FROM producto WHERE id_producto = 1;
SELECT id_boleta, subtotal, iva, total FROM boleta WHERE id_boleta = 1;

INSERT INTO boleta_detalle (id_boleta, id_producto, cantidad, precio_unitario, subtotal_linea)
VALUES (1, 3, 999, 1600, 0);

SELECT id_boleta, subtotal, iva, total FROM boleta WHERE id_boleta = 1;

SELECT * 
FROM boleta_detalle
WHERE id_boleta = 1 AND id_producto = 3;

INSERT INTO boleta_detalle (id_boleta, id_producto, cantidad, precio_unitario, subtotal_linea)
VALUES (1, 3, 1, 1600, 0);

SELECT id_boleta, subtotal, iva, total
FROM boleta
WHERE id_boleta = 1;

USE Cajero_Super;

CREATE OR REPLACE VIEW vw_boleta_completa AS
SELECT
  b.id_boleta, b.folio, b.fecha, b.estado,
  p.codigo_barra, p.nombre,
  d.cantidad, d.precio_unitario, d.subtotal_linea,
  b.subtotal, b.descuento, b.iva, b.total,
  b.metodo_pago, b.monto_pagado, b.vuelto
FROM boleta b
JOIN boleta_detalle d ON d.id_boleta = b.id_boleta
JOIN producto p ON p.id_producto = d.id_producto;

USE Cajero_Super;

SHOW FULL TABLES;

SELECT id_producto, codigo_barra, nombre, precio, stock, activo
FROM producto
WHERE activo = 1
ORDER BY id_producto;