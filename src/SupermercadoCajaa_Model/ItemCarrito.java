/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SupermercadoCajaa_Model;

/**
 *
 * @author julio
 */
public class ItemCarrito {

    private int idProducto;
    private String codigoBarra;
    private String nombre;
    private double precioUnitario;
    private int cantidad;

    public ItemCarrito(int idProducto, String codigoBarra, String nombre, double precioUnitario, int cantidad) {
        this.idProducto = idProducto;
        this.codigoBarra = codigoBarra;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    public int getIdProducto() { return idProducto; }
    public String getCodigoBarra() { return codigoBarra; }
    public String getNombre() { return nombre; }
    public double getPrecioUnitario() { return precioUnitario; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotalLinea() { return precioUnitario * cantidad; }

    @Override
    public String toString() {
        return String.format("%s | %s | $%.0f | cant:%d | sub:$%.0f",
                codigoBarra, nombre, precioUnitario, cantidad, getSubtotalLinea());
    }
}

