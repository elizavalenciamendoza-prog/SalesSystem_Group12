package util;

import model.Product;
import model.Seller;

import java.io.IOException;
import java.util.List;

/**
 * Main class for the first delivery of the project.
 * Generates pseudo-random input flat files for products, salesmen,
 * and salesman sales files.
 */
public class GenerateInfoFiles {

	/**
     * Program entry point.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Creando archivos de prueba para el Grupo 12
    	FileGenerator fileGenerator = new FileGenerator();

        try {
            int productsCount = 10;
            int salesManCount = 5;
            int salesLinesPerSalesMan = 6;

            List<Product> products = fileGenerator.createProductsFile(productsCount);
            List<Seller> salesMen = fileGenerator.createSalesManInfoFile(salesManCount);

            for (Seller salesMan : salesMen) {
                fileGenerator.createSalesMenFile(salesMan, products, salesLinesPerSalesMan);
            }

            System.out.println("Archivos generados correctamente en la carpeta data.");
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al generar archivos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error en los argumentos de generación: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }
}
