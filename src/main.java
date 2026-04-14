import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        try {
            System.out.println("--- SISTEMA DE VENTAS: PROCESAMIENTO SEMANA 5 ---");

            String rutaData = "src/data/";
            // Guarda el precio de cada producto usando su ID
            Map<String, Double> precios = new HashMap<>();

            // 1. LEER VENDEDORES
            File archivoVend = new File(rutaData + "salesMenInfo.txt");
            Scanner lectorVend = new Scanner(archivoVend);
            System.out.println("\nCargando Vendedores:");
            while (lectorVend.hasNextLine()) {
                System.out.println(" - " + lectorVend.nextLine());
            }
            lectorVend.close();

            // 2. LEER PRODUCTOS
            File archivoProd = new File(rutaData + "products.txt");
            Scanner lectorProd = new Scanner(archivoProd);
            System.out.println("\nCargando Catálogo de Productos:");
            while (lectorProd.hasNextLine()) {
                String[] datos = lectorProd.nextLine().split(";");
                System.out.println(" > " + datos[0] + ";" + datos[1] + ";" + datos[2]);
                if (datos.length == 3) {
                    String precioTexto = datos[2].trim().replace(',', '.');
                    precios.put(datos[0], Double.parseDouble(precioTexto));
                }
            }
            lectorProd.close();

            // 3. LEER VENTAS Y ACUMULADOR POR VENDEDOR
            System.out.println("\nCargando Ventas Registradas:");
            File[] archivosVentas = new File(rutaData).listFiles();

            if (archivosVentas != null) {
                for (File archivoVenta : archivosVentas) {
                    if (!archivoVenta.getName().startsWith("sales_") ||
                        !archivoVenta.getName().endsWith(".txt")) {
                        continue;
                    }

                    System.out.println("\n" + archivoVenta.getName());

                    Scanner lectorVentas = new Scanner(archivoVenta);

                    String idVendedor = "";
                    if (lectorVentas.hasNextLine()) {
                        idVendedor = lectorVentas.nextLine();
                        System.out.println(" * Vendedor: " + idVendedor);
                    }

                    double totalVendedor = 0.0;

                    while (lectorVentas.hasNextLine()) {
                        String linea = lectorVentas.nextLine().trim();
                        if (linea.isEmpty()) continue;

                        String[] datosVenta = linea.split(";");

                        if (datosVenta.length == 2) {
                            String idProducto = datosVenta[0];
                            int cantidad = Integer.parseInt(datosVenta[1]);
                            Double precio = precios.get(idProducto);

                            if (precio != null) {
                                double subtotal = precio * cantidad;
                                totalVendedor += subtotal; 
                                System.out.println(" * " + idProducto + ";" + cantidad + " = " + subtotal);
                            } else {
                                System.out.println(" * " + idProducto + " → precio no encontrado");
                            }
                        }
                    }
                    lectorVentas.close();

                    System.out.println(" TOTAL vendedor " + idVendedor + ": " + totalVendedor);
                }
            }
            // Mensaje de éxito obligatorio
            System.out.println("\nFinalización exitosa");

        } catch (Exception e) {
            System.err.println("Error al procesar los archivos: " + e.getMessage());
        }
    }
}