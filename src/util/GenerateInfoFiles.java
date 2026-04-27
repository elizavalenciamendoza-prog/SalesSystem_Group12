package util;

import model.Product;
import model.Seller;

import java.io.IOException;
import java.util.List;

/**
 * Main class for the input file generation process.
 */
public class GenerateInfoFiles {

    /**
     * Program entry point.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
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

            System.out.println("Creacion exitosa");
        } catch (IOException exception) {
            System.err.println("Error de entrada/salida al generar archivos: " + exception.getMessage());
        } catch (IllegalArgumentException exception) {
            System.err.println("Error en los argumentos de generación: " + exception.getMessage());
        } catch (Exception exception) {
            System.err.println("Error inesperado: " + exception.getMessage());
        }
    }
}
