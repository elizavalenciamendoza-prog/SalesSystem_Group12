import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import model.Product;
import model.Sale;
import model.Seller;

/**
 * Main processing class for the final delivery.
 * <p>
 * This program reads the generated input files, validates their content,
 * processes the sales, and creates the final report files required by the
 * project statement.
 */
public class main {

    private static final String DATA_FOLDER = "src/data";
    private static final String SALESMEN_INFO_FILE = "salesMenInfo.txt";
    private static final String PRODUCTS_FILE = "products.txt";
    private static final String SALES_REPORT_FILE = "salesmenReport.csv";
    private static final String PRODUCTS_REPORT_FILE = "productsReport.csv";
    private static final String ERROR_LOG_FILE = "errorLog.txt";

    /**
     * Program entry point.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            File dataDirectory = new File(DATA_FOLDER);
            validateDataDirectory(dataDirectory);

            List<String> validationErrors = new ArrayList<String>();
            Map<String, Seller> sellersByKey = loadSellers(dataDirectory, validationErrors);
            Map<String, Product> productsById = loadProducts(dataDirectory, validationErrors);

            processSalesFiles(dataDirectory, sellersByKey, productsById, validationErrors);
            writeSalesmenReport(dataDirectory, sellersByKey);
            writeProductsReport(dataDirectory, productsById);
            writeErrorLog(dataDirectory, validationErrors);

            System.out.println("Finalización exitosa");
        } catch (Exception exception) {
            System.err.println("Error al procesar los archivos: " + exception.getMessage());
        }
    }

    private static void validateDataDirectory(File dataDirectory) {
        if (!dataDirectory.exists() || !dataDirectory.isDirectory()) {
            throw new IllegalStateException("La carpeta de datos no existe en la ruta: " + DATA_FOLDER);
        }
    }

    private static Map<String, Seller> loadSellers(File dataDirectory, List<String> validationErrors) throws IOException {
        File sellersFile = new File(dataDirectory, SALESMEN_INFO_FILE);
        if (!sellersFile.exists()) {
            throw new IOException("No se encontró el archivo de vendedores: " + sellersFile.getPath());
        }

        Map<String, Seller> sellersByKey = new LinkedHashMap<String, Seller>();
        Scanner scanner = new Scanner(sellersFile, "UTF-8");

        try {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] fields = splitLine(line);
                if (fields.length != 4) {
                    validationErrors.add("Línea inválida en salesMenInfo.txt (línea " + lineNumber + "): " + line);
                    continue;
                }

                try {
                    String documentType = fields[0];
                    long documentNumber = Long.parseLong(fields[1]);
                    String firstName = fields[2];
                    String lastName = fields[3];
                    String sellerKey = buildSellerKey(documentType, documentNumber);

                    if (sellersByKey.containsKey(sellerKey)) {
                        validationErrors.add("Vendedor duplicado en salesMenInfo.txt: " + sellerKey);
                        continue;
                    }

                    sellersByKey.put(sellerKey, new Seller(documentType, documentNumber, firstName, lastName));
                } catch (NumberFormatException exception) {
                    validationErrors.add("Número de documento inválido en salesMenInfo.txt (línea " + lineNumber + "): " + line);
                }
            }
        } finally {
            scanner.close();
        }

        if (sellersByKey.isEmpty()) {
            throw new IllegalStateException("No se cargó ningún vendedor válido desde salesMenInfo.txt");
        }

        return sellersByKey;
    }

    private static Map<String, Product> loadProducts(File dataDirectory, List<String> validationErrors) throws IOException {
        File productsFile = new File(dataDirectory, PRODUCTS_FILE);
        if (!productsFile.exists()) {
            throw new IOException("No se encontró el archivo de productos: " + productsFile.getPath());
        }

        Map<String, Product> productsById = new LinkedHashMap<String, Product>();
        Scanner scanner = new Scanner(productsFile, "UTF-8");

        try {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] fields = splitLine(line);
                if (fields.length != 3) {
                    validationErrors.add("Línea inválida en products.txt (línea " + lineNumber + "): " + line);
                    continue;
                }

                try {
                    String productId = fields[0];
                    String productName = fields[1];
                    double price = parseDecimal(fields[2]);

                    if (price < 0) {
                        validationErrors.add("Precio negativo detectado en products.txt (línea " + lineNumber + "): " + line);
                        continue;
                    }

                    if (productsById.containsKey(productId)) {
                        validationErrors.add("Producto duplicado en products.txt: " + productId);
                        continue;
                    }

                    productsById.put(productId, new Product(productId, productName, price));
                } catch (NumberFormatException exception) {
                    validationErrors.add("Precio inválido en products.txt (línea " + lineNumber + "): " + line);
                }
            }
        } finally {
            scanner.close();
        }

        if (productsById.isEmpty()) {
            throw new IllegalStateException("No se cargó ningún producto válido desde products.txt");
        }

        return productsById;
    }

    private static void processSalesFiles(File dataDirectory,
                                          Map<String, Seller> sellersByKey,
                                          Map<String, Product> productsById,
                                          List<String> validationErrors) throws IOException {
        File[] salesFiles = dataDirectory.listFiles();
        if (salesFiles == null) {
            throw new IOException("No fue posible listar los archivos de la carpeta data.");
        }

        for (File salesFile : salesFiles) {
            if (!isSalesFile(salesFile)) {
                continue;
            }

            processSingleSalesFile(salesFile, sellersByKey, productsById, validationErrors);
        }
    }

    private static boolean isSalesFile(File file) {
        return file.isFile()
                && file.getName().startsWith("sales_")
                && file.getName().endsWith(".txt")
                && !SALESMEN_INFO_FILE.equals(file.getName());
    }

    private static void processSingleSalesFile(File salesFile,
                                               Map<String, Seller> sellersByKey,
                                               Map<String, Product> productsById,
                                               List<String> validationErrors) throws IOException {
        Scanner scanner = new Scanner(salesFile, "UTF-8");

        try {
            if (!scanner.hasNextLine()) {
                validationErrors.add("Archivo de ventas vacío: " + salesFile.getName());
                return;
            }

            String headerLine = scanner.nextLine().trim();
            String[] headerFields = splitLine(headerLine);

            if (headerFields.length != 2) {
                validationErrors.add("Encabezado inválido en archivo de ventas " + salesFile.getName() + ": " + headerLine);
                return;
            }

            Seller seller;
            try {
                String sellerKey = buildSellerKey(headerFields[0], Long.parseLong(headerFields[1]));
                seller = sellersByKey.get(sellerKey);
            } catch (NumberFormatException exception) {
                validationErrors.add("Documento inválido en el encabezado del archivo " + salesFile.getName() + ": " + headerLine);
                return;
            }

            if (seller == null) {
                validationErrors.add("Vendedor no encontrado para el archivo " + salesFile.getName() + ": " + headerLine);
                return;
            }

            int lineNumber = 1;
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] fields = splitLine(line);
                if (fields.length != 2) {
                    validationErrors.add("Línea inválida en " + salesFile.getName() + " (línea " + lineNumber + "): " + line);
                    continue;
                }

                String productId = fields[0];
                Product product = productsById.get(productId);
                if (product == null) {
                    validationErrors.add("Producto inexistente en " + salesFile.getName() + " (línea " + lineNumber + "): " + productId);
                    continue;
                }

                try {
                    int quantity = Integer.parseInt(fields[1]);
                    if (quantity < 0) {
                        validationErrors.add("Cantidad negativa en " + salesFile.getName() + " (línea " + lineNumber + "): " + line);
                        continue;
                    }

                    seller.addSale(new Sale(product, quantity));
                } catch (NumberFormatException exception) {
                    validationErrors.add("Cantidad inválida en " + salesFile.getName() + " (línea " + lineNumber + "): " + line);
                }
            }
        } finally {
            scanner.close();
        }
    }

    private static void writeSalesmenReport(File dataDirectory, Map<String, Seller> sellersByKey) throws IOException {
        List<Seller> sellers = new ArrayList<Seller>(sellersByKey.values());
        Collections.sort(sellers, new Comparator<Seller>() {
            @Override
            public int compare(Seller firstSeller, Seller secondSeller) {
                return Double.compare(secondSeller.getTotalSalesAmount(), firstSeller.getTotalSalesAmount());
            }
        });

        File reportFile = new File(dataDirectory, SALES_REPORT_FILE);
        BufferedWriter writer = new BufferedWriter(new FileWriter(reportFile));
        DecimalFormat decimalFormat = createDecimalFormat();

        try {
            for (Seller seller : sellers) {
                writer.write(seller.getDocumentType() + ";"
                        + seller.getId() + ";"
                        + seller.getFirstName() + ";"
                        + seller.getLastName() + ";"
                        + decimalFormat.format(seller.getTotalSalesAmount()));
                writer.newLine();
            }
        } finally {
            writer.close();
        }
    }

    private static void writeProductsReport(File dataDirectory, Map<String, Product> productsById) throws IOException {
        List<Product> soldProducts = new ArrayList<Product>();
        for (Product product : productsById.values()) {
            if (product.getTotalQuantitySold() > 0) {
                soldProducts.add(product);
            }
        }

        Collections.sort(soldProducts, new Comparator<Product>() {
            @Override
            public int compare(Product firstProduct, Product secondProduct) {
                return Integer.compare(secondProduct.getTotalQuantitySold(), firstProduct.getTotalQuantitySold());
            }
        });

        File reportFile = new File(dataDirectory, PRODUCTS_REPORT_FILE);
        BufferedWriter writer = new BufferedWriter(new FileWriter(reportFile));
        DecimalFormat decimalFormat = createDecimalFormat();

        try {
            for (Product product : soldProducts) {
                writer.write(product.getProductName() + ";"
                        + decimalFormat.format(product.getPrice()) + ";"
                        + product.getTotalQuantitySold());
                writer.newLine();
            }
        } finally {
            writer.close();
        }
    }

    private static void writeErrorLog(File dataDirectory, List<String> validationErrors) throws IOException {
        File errorLogFile = new File(dataDirectory, ERROR_LOG_FILE);
        BufferedWriter writer = new BufferedWriter(new FileWriter(errorLogFile));

        try {
            if (validationErrors.isEmpty()) {
                writer.write("No se encontraron errores de validación.");
                writer.newLine();
                return;
            }

            for (String validationError : validationErrors) {
                writer.write(validationError);
                writer.newLine();
            }
        } finally {
            writer.close();
        }
    }

    private static String buildSellerKey(String documentType, long documentNumber) {
        return documentType.trim() + ";" + documentNumber;
    }

    private static String[] splitLine(String line) {
        String sanitizedLine = line;
        if (sanitizedLine.endsWith(";")) {
            sanitizedLine = sanitizedLine.substring(0, sanitizedLine.length() - 1);
        }

        String[] rawFields = sanitizedLine.split(";");
        List<String> cleanFields = new ArrayList<String>();
        for (String rawField : rawFields) {
            String field = rawField.trim();
            if (!field.isEmpty()) {
                cleanFields.add(field);
            }
        }

        return cleanFields.toArray(new String[cleanFields.size()]);
    }

    private static double parseDecimal(String value) {
        return Double.parseDouble(value.replace(',', '.'));
    }

    private static DecimalFormat createDecimalFormat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        return new DecimalFormat("0.00", symbols);
    }
}
