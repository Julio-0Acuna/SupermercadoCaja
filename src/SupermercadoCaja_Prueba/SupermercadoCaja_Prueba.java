/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package SupermercadoCaja_Prueba;

/**
 *
 * @author julio
 */
import supermercadocaja.DB.Conexion;
import java.sql.Connection;

public class SupermercadoCaja_Prueba {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try (Connection con = Conexion.getConnection()) {
            System.out.println("✅ Conexión OK a Cajero_Super");
        } catch (Exception e) {
            System.out.println("❌ Error conexión: " + e.getMessage());
        }
    }
    
}
