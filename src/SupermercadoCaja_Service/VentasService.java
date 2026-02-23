/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SupermercadoCaja_Service;

/**
 *
 * @author Julio Acuña
 */

import SupermercadoCaja.DAO.BoletaDAO;
import SupermercadoCaja.DAO.BoletaDetalleDAO;
import SupermercadoCaja.DAO.ProductoDAO;
import SupermercadoCajaa_Model.ItemCarrito;
import SupermercadoCajaa_Model.Producto;
import java.util.ArrayList;
import java.util.List;

public class VentasService {
    
private final ProductoDAO productoDAO = new ProductoDAO();
    private final BoletaDAO boletaDAO = new BoletaDAO();
    private final BoletaDetalleDAO detalleDAO = new BoletaDetalleDAO();

    private final List<ItemCarrito> carrito = new ArrayList<>();
    private Integer idBoletaActual = null;

    public void nuevaVenta() {
        carrito.clear();
        idBoletaActual = null;
    }

    public List<ItemCarrito> getCarrito() { return carrito; }
    public Integer getIdBoletaActual() { return idBoletaActual; }

    public void agregarPorCodigo(String codigoBarra, int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("Cantidad debe ser > 0");

        Producto p = productoDAO.buscarPorCodigoBarra(codigoBarra);
        if (p == null) throw new IllegalArgumentException("Producto no existe o está inactivo.");

        for (ItemCarrito item : carrito) {
            if (item.getIdProducto() == p.getIdProducto()) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }

        carrito.add(new ItemCarrito(p.getIdProducto(), p.getCodigoBarra(), p.getNombre(), p.getPrecio(), cantidad));
    }

    public double totalCarrito() {
        return carrito.stream().mapToDouble(ItemCarrito::getSubtotalLinea).sum();
    }

    public int cerrarVenta(String metodoPago, double montoPagado, double descuento) {
        if (carrito.isEmpty()) throw new IllegalStateException("Carrito vacío.");

        int idBoleta = boletaDAO.crearBoleta(metodoPago, montoPagado, descuento);

        for (ItemCarrito item : carrito) {
            detalleDAO.agregarLinea(idBoleta, item.getIdProducto(), item.getCantidad(), item.getPrecioUnitario());
        }

        idBoletaActual = idBoleta;
        return idBoleta;
    }

    public BoletaDAO.BoletaResumen resumenBoletaActual() {
        if (idBoletaActual == null) return null;
        return boletaDAO.obtenerResumen(idBoletaActual);
    }
}
