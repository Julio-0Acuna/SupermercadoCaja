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
import SupermercadoCajaa_Model.Producto;
import java.sql.*;

public class ProductoDAO {

    public Producto buscarPorCodigoBarra(String codigo) {
        String sql = "SELECT id_producto, codigo_barra, nombre, precio, stock, activo " +
                     "FROM producto WHERE codigo_barra = ? AND activo = 1";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("id_producto"));
                    p.setCodigoBarra(rs.getString("codigo_barra"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setStock(rs.getInt("stock"));
                    p.setActivo(rs.getInt("activo") == 1);
                    return p;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error buscarPorCodigoBarra: " + e.getMessage());
        }
        return null;
    }
}