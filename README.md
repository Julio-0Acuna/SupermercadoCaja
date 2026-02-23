# 🏪 Cajero_Super - Sistema de Caja Registradora

Cajero_Super es un sistema de punto de venta (POS) desarrollado en Java utilizando JDBC y MySQL.

El sistema permite gestionar ventas en un supermercado mediante una caja registradora por consola, con control automático de inventario, cálculo de impuestos (IVA 19%) y generación de tickets en formato TXT.

---

## 🚀 Características Principales

- Gestión de productos
- Control automático de stock mediante triggers
- Cálculo automático de subtotal, IVA y total
- Validación de stock insuficiente
- Eliminación lógica de productos
- Generación de boleta (cabecera + detalle)
- Exportación de ticket en archivo `.txt`
- Arquitectura por capas (DAO / Service / Model)

---

## 🛠 Tecnologías Utilizadas

- Java (JDK 17+)
- JDBC
- MySQL 8+
- NetBeans (Proyecto Java Ant)
- MySQL Workbench

---

## 🗄 Base de Datos

Base de datos: `Cajero_Super`

Incluye:

- Tabla `producto`
- Tabla `boleta`
- Tabla `boleta_detalle`
- Triggers para:
  - Cálculo automático de subtotal por línea
  - Descuento automático de stock
  - Recalculo automático de totales
- Vista `vw_boleta_resumen`
- Vista `vw_boleta_completa`

---

## 💻 Funcionalidad de la Caja

El sistema permite:

1. Agregar productos por código de barra
2. Visualizar carrito de compra
3. Cerrar venta indicando método de pago
4. Calcular vuelto automáticamente
5. Exportar ticket en formato TXT

---

## 📦 Estructura del Proyecto

src/
├── app/
├── dao/
├── db/
├── model/
└── service/

## 📄 Ejemplo de Ticket Generado

====================================
CAJERO SUPER
Folio : BOL-12345678
Pan
2 x $1200 = $2400
Subtotal : $2400
IVA (19%): $456
TOTAL : $2856

## ▶ Cómo Ejecutar

1. Crear base de datos `Cajero_Super` en MySQL
2. Ejecutar script SQL incluido
3. Configurar credenciales en `Conexion.java`
4. Ejecutar `CajaRegistradora.java`
5. Generar venta y exportar ticket

---

## 📚 Autor

Desarrollado por Julio Acuña 
Proyecto académico y de portafolio.
