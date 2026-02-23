/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SupermercadoCaja_App;

/**
 *
 * @author Julio Acuña
 */
import SupermercadoCaja.DAO.BoletaDAO;
import SupermercadoCajaa_Model.ItemCarrito;
import SupermercadoCaja_Service.SupermercadoService_TicketService;
import SupermercadoCaja_Service.VentasService;
import java.util.Scanner;

public class CajaRegistradora {
    

public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        VentasService venta = new VentasService();
        SupermercadoService_TicketService ticketService = new SupermercadoService_TicketService();

        venta.nuevaVenta();

        int op;
        do {
            System.out.println("\n=== CAJERO SUPER | CAJA REGISTRADORA ===");
            System.out.println("1) Agregar producto (código de barra)");
            System.out.println("2) Ver carrito");
            System.out.println("3) Cerrar venta (pagar)");
            System.out.println("4) Exportar ticket TXT (última boleta)");
            System.out.println("5) Nueva venta");
            System.out.println("0) Salir");
            System.out.print("Opción: ");

            op = leerInt(sc);

            try {
                switch (op) {
                    case 1 -> agregarProducto(sc, venta);
                    case 2 -> verCarrito(venta);
                    case 3 -> cerrarVenta(sc, venta);
                    case 4 -> exportarTicket(venta, ticketService);
                    case 5 -> {
                        venta.nuevaVenta();
                        System.out.println("✅ Nueva venta iniciada.");
                    }
                    case 0 -> System.out.println("Saliendo...");
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("❌ " + e.getMessage());
            }

        } while (op != 0);
    }

    private static void agregarProducto(Scanner sc, VentasService venta) {
        System.out.print("Código de barra: ");
        String codigo = sc.next().trim();

        System.out.print("Cantidad: ");
        int cantidad = leerInt(sc);

        venta.agregarPorCodigo(codigo, cantidad);
        System.out.println("✅ Producto agregado al carrito.");
    }

    private static void verCarrito(VentasService venta) {
        System.out.println("\n--- CARRITO ---");
        if (venta.getCarrito().isEmpty()) {
            System.out.println("Carrito vacío.");
            return;
        }

        for (ItemCarrito item : venta.getCarrito()) {
            System.out.println(item);
        }

        System.out.println("------------------------");
        System.out.printf("Pre-total (suma líneas): $%.0f%n", venta.totalCarrito());
    }

    private static void cerrarVenta(Scanner sc, VentasService venta) {
        if (venta.getCarrito().isEmpty()) {
            System.out.println("Carrito vacío.");
            return;
        }

        System.out.print("Método pago (EFECTIVO/DEBITO/CREDITO/TRANSFERENCIA): ");
        String metodo = sc.next().trim().toUpperCase();

        System.out.print("Descuento ($): ");
        double descuento = leerDouble(sc);

        System.out.print("Monto pagado ($): ");
        double pagado = leerDouble(sc);

        int idBoleta = venta.cerrarVenta(metodo, pagado, descuento);
        BoletaDAO.BoletaResumen r = venta.resumenBoletaActual();

        System.out.println("\n✅ Venta cerrada.");
        System.out.println("Boleta ID: " + idBoleta);
        System.out.println("Folio: " + r.folio());
        System.out.printf("Subtotal: $%.0f | IVA: $%.0f | Total: $%.0f%n", r.subtotal(), r.iva(), r.total());
        System.out.printf("Pagado: $%.0f | Vuelto: $%.0f%n", r.montoPagado(), r.vuelto());
    }

    private static void exportarTicket(VentasService venta, SupermercadoService_TicketService ticketService) {
        Integer id = venta.getIdBoletaActual();
        if (id == null) {
            System.out.println("Aún no hay boleta cerrada para exportar.");
            return;
        }

        String ruta = ticketService.exportarTicketTxt(id, "tickets");
        System.out.println("✅ Ticket exportado: " + ruta);
    }

    private static int leerInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Ingrese un entero válido: ");
            sc.next();
        }
        return sc.nextInt();
    }

    private static double leerDouble(Scanner sc) {
        while (!sc.hasNextDouble()) {
            System.out.print("Ingrese un número válido: ");
            sc.next();
        }
        return sc.nextDouble();
    }
}