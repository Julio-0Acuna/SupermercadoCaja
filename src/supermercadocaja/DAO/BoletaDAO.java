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
import java.sql.*;
import java.util.UUID;

public class BoletaDAO {

    public int crearBoleta(String metodoPago, double montoPagado, double descuento) {
        String folio = "BOL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String sql = "INSERT INTO boleta (folio, metodo_pago, monto_pagado, descuento) VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, folio);
            ps.setString(2, metodoPago);
            ps.setDouble(3, montoPagado);
            ps.setDouble(4, descuento);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            throw new SQLException("No se pudo obtener ID boleta.");

        } catch (SQLException e) {
            throw new RuntimeException("Error crearBoleta: " + e.getMessage(), e);
        }
    }

    public BoletaResumen obtenerResumen(int idBoleta) {
        String sql = "SELECT id_boleta, folio, fecha, subtotal, descuento, iva, total, metodo_pago, monto_pagado, vuelto, estado " +
                     "FROM vw_boleta_resumen WHERE id_boleta = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idBoleta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new BoletaResumen(
                            rs.getInt("id_boleta"),
                            rs.getString("folio"),
                            rs.getTimestamp("fecha"),
                            rs.getDouble("subtotal"),
                            rs.getDouble("descuento"),
                            rs.getDouble("iva"),
                            rs.getDouble("total"),
                            rs.getString("metodo_pago"),
                            rs.getDouble("monto_pagado"),
                            rs.getDouble("vuelto"),
                            rs.getString("estado")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error obtenerResumen: " + e.getMessage(), e);
        }
    }

    public record BoletaResumen(
            int idBoleta,
            String folio,
            Timestamp fecha,
            double subtotal,
            double descuento,
            double iva,
            double total,
            String metodoPago,
            double montoPagado,
            double vuelto,
            String estado
    ) {}
}