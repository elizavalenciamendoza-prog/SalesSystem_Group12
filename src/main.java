import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.Product;
import model.Seller;
import model.Sale;

public class main {
    public static void main(String[] args) {
        try {
            System.out.println("--- SISTEMA DE VENTAS: PROCESAMIENTO SEMANA 5 ---");

            // Ruta hacia la carpeta data
            String rutaData = "src/data/";

            // 1. LEER VENDEDORES
            File archivoVend = new File(rutaData + "salesMenInfo.txt");
            Scanner lectorVend = new Scanner(archivoVend);
            System.out.println("\nCargando Vendedores:");
            List<Seller> vendedores = new ArrayList<>();
            while (lectorVend.hasNextLine()) {
                String linea = lectorVend.nextLine();
                // Primero dividimos la línea en partes y luego creamos el objeto Vendedor
                String[] partes = linea.split(";");

                // Validamos que la línea tenga el formato correcto antes de crear el objeto
                if (partes.length == 4) {
                    String tipoId = partes[0].trim();
                    // Creamos el objeto Vendedor con los datos extraídos
                    String id = partes[1].trim();
                    String nombre = partes[2].trim();
                    String apellido = partes[3].trim();
                    Seller vendedor = new Seller(tipoId, Long.parseLong(id), nombre, apellido);
                    vendedores.add(vendedor);
                }
            }
            lectorVend.close();

            // 2. LEER PRODUCTOS
            List<Product> productos = new ArrayList<>();
            File archivoProd = new File(rutaData + "products.txt");
            Scanner lectorProd = new Scanner(archivoProd);
            System.out.println("Cargando Catálogo de Productos:");
            while (lectorProd.hasNextLine()) {
                String linea = lectorProd.nextLine();
                String[] partes = linea.split(";");

                if (partes.length == 3) {
                    String id = partes[0].trim();
                    String nombre = partes[1].trim();
                    double precio = Double.parseDouble(partes[2].trim().replace(",", ".")); // En caso de que el precio use coma como separador decimal reemplazamos por punto
                    Product producto = new Product(id, nombre, precio);
                    productos.add(producto);
                }
            }
            lectorProd.close();

            // 3. LEER VENTAS
            File carpetaData = new File(rutaData);
            File[] archivosVentas = carpetaData.listFiles(
                // Filtramos solo los archivos que corresponden a ventas
                archivo -> archivo.isFile() && archivo.getName().startsWith("sales_") && archivo.getName().endsWith(".txt")
            );

            System.out.println("Cargando Ventas:");
            if (archivosVentas != null) {
                // Procesamos cada archivo de ventas
                for (File archivoVentas : archivosVentas) {
                    Scanner lectorVentas = new Scanner(archivoVentas);

                    if (!lectorVentas.hasNextLine()) {
                        lectorVentas.close();
                        continue;
                    }

                    String linea = lectorVentas.nextLine();
                    Seller vendedor = null;
                    // La primera línea del archivo de ventas contiene la información del vendedor
                    String[] partesVendedor = linea.split(";");

                    if (partesVendedor.length == 2) {
                        String tipoId = partesVendedor[0].trim();
                        String id = partesVendedor[1].trim();

                        // Buscamos el vendedor correspondiente en la lista de vendedores
                        for (Seller v : vendedores) {
                            if (v.getDocumentType().equals(tipoId) && String.valueOf(v.getId()).equals(id)) {
                                vendedor = v;
                                break;
                            }
                        }
                    }

                    // Leemos las ventas del vendedor actual
                    while (lectorVentas.hasNextLine()) {
                        linea = lectorVentas.nextLine();
                        String[] partes = linea.split(";");

                        if (partes.length == 2 && vendedor != null) {
                            String idProducto = partes[0].trim();
                            int cantidad = Integer.parseInt(partes[1].trim());

                            Product producto = null;
                            // Buscamos el producto en el catálogo
                            for (Product p : productos) {
                                if (p.getId().equals(idProducto)) {
                                    producto = p;
                                    break;
                                }
                            }

                            if (producto != null) {
                                // Creamos la venta y la asociamos al vendedor
                                Sale venta = new Sale(producto, cantidad);
                                vendedor.addSale(venta);
                            }
                        }
                    }

                    lectorVentas.close();
                }
            }

            System.out.println("\n--- INFORMACIÓN DE VENDEDORES Y SUS VENTAS ---");

            for (Seller v : vendedores) {
            	System.out.println("Vendedor");
                v.printInfo();
            }

            // Mensaje de éxito obligatorio
            System.out.println("\nFinalización exitosa");

        } catch (Exception e) {
            System.err.println("Error al procesar los archivos: " + e.getMessage());
        }
    }
}