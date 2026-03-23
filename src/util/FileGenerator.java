package util;

import model.Product;
import model.Seller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Generates input files for the salesman sales project.
 */
public class FileGenerator {

    private static final String OUTPUT_FOLDER = "src/data";
    private static final String PRODUCTS_FILE_NAME = "products.txt";
    private static final String SALESMEN_FILE_NAME = "salesMenInfo.txt";

    private final DecimalFormat decimalFormat;

    public FileGenerator() {
        decimalFormat = new DecimalFormat("0.00");
    }

    /**
     * Creates the products file with pseudo-random product data.
     *
     * @param productsCount number of products to generate
     * @return generated list of products
     * @throws IOException if an I/O error occurs
     */
    public List<Product> createProductsFile(int productsCount) throws IOException {
        validatePositiveNumber(productsCount, "productsCount");
        createOutputFolderIfNeeded();

        List<Product> products = new ArrayList<Product>();
        File file = new File(OUTPUT_FOLDER, PRODUCTS_FILE_NAME);

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        try {
            for (int i = 0; i < productsCount; i++) {
                String productId = "P" + String.format("%03d", i + 1);
                String productName = RandomDataUtil.randomProductName(i);
                double price = RandomDataUtil.randomPrice();

                Product product = new Product(productId, productName, price);
                products.add(product);

                writer.write(product.getId() + ";" + product.getProductName() + ";" + decimalFormat.format(product.getPrice()));
                writer.newLine();
            }
        } finally {
            writer.close();
        }

        return products;
    }

    /**
     * Creates the salesman info file with pseudo-random salesman data.
     *
     * @param salesmanCount number of salesmen to generate
     * @return generated list of salesmen
     * @throws IOException if an I/O error occurs
     */
    public List<Seller> createSalesManInfoFile(int salesmanCount) throws IOException {
        validatePositiveNumber(salesmanCount, "salesmanCount");
        createOutputFolderIfNeeded();

        List<Seller> salesMen = new ArrayList<>();
        File file = new File(OUTPUT_FOLDER, SALESMEN_FILE_NAME);

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        try {
            for (int i = 0; i < salesmanCount; i++) {
                String documentType = RandomDataUtil.randomDocumentType();
                long documentNumber = 1000 + i;
                String firstName = RandomDataUtil.randomFirstName();
                String lastName = RandomDataUtil.randomLastName();

                Seller salesMan = new Seller(documentType, documentNumber, firstName, lastName);
                salesMen.add(salesMan);

                writer.write(salesMan.getDocumentType() + ";" +
                             salesMan.getId() + ";" +
                             salesMan.getFirstName() + ";" +
                             salesMan.getLastName());
                writer.newLine();
            }
        } finally {
            writer.close();
        }

        return salesMen;
    }

    /**
     * Creates a pseudo-random sales file for one salesman.
     * This method is included to comply with the project statement.
     *
     * @param randomSalesCount number of sales lines
     * @param name salesman name used in file name
     * @param id salesman id
     * @throws IOException if an I/O error occurs
     */
    public void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
        validatePositiveNumber(randomSalesCount, "randomSalesCount");
        createOutputFolderIfNeeded();

        File file = new File(OUTPUT_FOLDER, "sales_" + sanitizeFileName(name) + "_" + id + ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        try {
            writer.write("CC;" + id);
            writer.newLine();

            for (int i = 0; i < randomSalesCount; i++) {
                String productId = "P" + String.format("%03d", 1 + RandomDataUtil.randomProductIndex(10));
                int quantity = RandomDataUtil.randomQuantity();

                writer.write(productId + ";" + quantity);
                writer.newLine();
            }
        } finally {
            writer.close();
        }
    }

    /**
     * Recommended overloaded method to create coherent salesman sales files
     * using an existing salesman and the generated products list.
     *
     * @param salesMan salesman owner of the file
     * @param products available products
     * @param randomSalesCount number of sales lines
     * @throws IOException if an I/O error occurs
     */
    public void createSalesMenFile(Seller salesMan, List<Product> products, int randomSalesCount) throws IOException {
        validatePositiveNumber(randomSalesCount, "randomSalesCount");

        if (salesMan == null) {
            throw new IllegalArgumentException("salesMan must not be null");
        }

        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("products must not be null or empty");
        }

        createOutputFolderIfNeeded();

        String fileName = "sales_" + salesMan.getId() + ".txt";
        File file = new File(OUTPUT_FOLDER, fileName);

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        Set<String> usedProducts = new HashSet<String>();

        try {
            writer.write(salesMan.getDocumentType() + ";" + salesMan.getId());
            writer.newLine();

            for (int i = 0; i < randomSalesCount; i++) {
                Product product = products.get(RandomDataUtil.randomProductIndex(products.size()));

                /*
                 * Avoid repeating too many product lines when enough products exist.
                 */
                if (usedProducts.size() < products.size()) {
                    while (usedProducts.contains(product.getId())) {
                        product = products.get(RandomDataUtil.randomProductIndex(products.size()));
                    }
                    usedProducts.add(product.getId());
                }

                int quantity = RandomDataUtil.randomQuantity();
                writer.write(product.getId() + ";" + quantity);
                writer.newLine();
            }
        } finally {
            writer.close();
        }
    }

    private void createOutputFolderIfNeeded() {
        File folder = new File(OUTPUT_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private void validatePositiveNumber(int value, String argumentName) {
        if (value <= 0) {
            throw new IllegalArgumentException(argumentName + " must be greater than zero");
        }
    }

    private String sanitizeFileName(String value) {
        return value.replaceAll("[^a-zA-Z0-9-_]", "_");
    }
}
