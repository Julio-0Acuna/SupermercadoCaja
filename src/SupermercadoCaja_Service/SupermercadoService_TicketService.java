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
import supermercadocaja.DB.Conexion;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;

public class SupermercadoService_TicketService {
    

 public String exportarTicketTxt(int idBoleta, String carpeta) {
        BoletaDAO boletaDAO = new BoletaDAO();
        BoletaDAO.BoletaResumen r = boletaDAO.obtenerResumen(idBoleta);

        if (r == null) throw new IllegalArgumentException("No existe la boleta: " + idBoleta);

        // Crear carpeta si no existe
        File dir = new File(carpeta);
        if (!dir.exists()) dir.mkdirs();

        String fileName = carpeta + File.separator + "ticket_" + r.folio() + ".txt";

        try (FileWriter fw = new FileWriter(fileName)) {

            fw.write("====================================\n");
            fw.write("            CAJERO SUPER            \n");
            fw.write("====================================\n");
            fw.write("Folio : " + r.folio() + "\n");
            fw.write("Fecha : " + r.fecha().toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
            fw.write("Estado: " + r.estado() + "\n");
            fw.write("------------------------------------\n");
            fw.write("ITEMS\n");

            String sql = "SELECT codigo_barra, nombre, cantidad, precio_unitario, subtotal_linea " +
                         "FROM vw_boleta_completa WHERE id_boleta = ? ORDER BY nombre";

            try (Connection con = Conexion.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, idBoleta);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String cod = rs.getString("codigo_barra");
                        String nom = rs.getString("nombre");
                        int cant = rs.getInt("cantidad");
                        double pu = rs.getDouble("precio_unitario");
                        double sub = rs.getDouble("subtotal_linea");

                        fw.write(String.format("- %s | %s\n", cod, nom));
                        fw.write(String.format("  %d x $%.0f = $%.0f\n", cant, pu, sub));
                    }
                }
            }

            fw.write("------------------------------------\n");
            fw.write(String.format("Subtotal : $%.0f\n", r.subtotal()));
            fw.write(String.format("Descuento: $%.0f\n", r.descuento()));
            fw.write(String.format("IVA (19%): $%.0f\n", r.iva()));
            fw.write(String.format("TOTAL    : $%.0f\n", r.total()));
            fw.write("------------------------------------\n");
            fw.write("Pago: " + r.metodoPago() + "\n");
            fw.write(String.format("Pagado: $%.0f\n", r.montoPagado()));
            fw.write(String.format("Vuelto: $%.0f\n", r.vuelto()));
            fw.write("====================================\n");
            fw.write("Gracias por su compra!\n");

            return fileName;

        } catch (Exception e) {
            throw new RuntimeException("Error exportarTicketTxt: " + e.getMessage(), e);
        }
    }
}