import java.io.FileWriter;
import java.io.PrintWriter;

public class GenerateInfoFiles {

    public static void main(String[] args) {
        // Creando archivos de prueba para el Grupo 12
        createProductsFile();
        createSellersFile();
        System.out.println("Archivos creados exitosamente en la carpeta del proyecto.");
    }

    public static void createProductsFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("products.txt"))) {
            writer.println("1;Laptop;2500.0");
            writer.println("2;Mouse;25.0");
            writer.println("3;Keyboard;45.0");
        } catch (Exception e) {
            System.out.println("Error creating products file");
        }
    }

    public static void createSellersFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("sellers.txt"))) {
            writer.println("101;John;Doe");
            writer.println("102;Jane;Smith");
        } catch (Exception e) {
            System.out.println("Error creating sellers file");
        }
    }
}