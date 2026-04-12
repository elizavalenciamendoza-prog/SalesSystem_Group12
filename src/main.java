import java.io.File;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        try {
            System.out.println("--- SISTEMA DE VENTAS: PROCESAMIENTO SEMANA 5 ---");

            // Ruta hacia la carpeta data que veo en tu Eclipse
            String rutaData = "src/data/";

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
                System.out.println(" > " + lectorProd.nextLine());
            }
            lectorProd.close();

            // Mensaje de éxito obligatorio
            System.out.println("\nFinalización exitosa");

        } catch (Exception e) {
            System.err.println("Error al procesar los archivos: " + e.getMessage());
        }
    }
}