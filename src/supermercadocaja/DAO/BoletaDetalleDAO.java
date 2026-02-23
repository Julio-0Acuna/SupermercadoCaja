/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SupermercadoCaja.DAO;

/**
 *
 * @author Julio Acuña
 */

import supermercadocaja.DB.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BoletaDetalleDAO {
    public void agregarLinea(int idBoleta, int idProducto, int cantidad, double precioUnitario) {
        String sql = "INSERT INTO boleta_detalle (id_boleta, id_producto, cantidad, precio_unitario, subtotal_linea) " +
                     "VALUES (?, ?, ?, ?, 0)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idBoleta);
            ps.setInt(2, idProducto);
            ps.setInt(3, cantidad);
            ps.setDouble(4, precioUnitario);

            ps.executeUpdate(); // triggers: calcula subtotal_linea, descuenta stock y recalcula boleta

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}