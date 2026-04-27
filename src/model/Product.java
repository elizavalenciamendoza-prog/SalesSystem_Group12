package model;

/**
 * Represents a product available for sale.
 */
public class Product {

    private final String id;
    private final String productName;
    private final double price;
    private int totalQuantitySold;

    public Product(String id, String productName, double price) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.totalQuantitySold = 0;
    }

    public String getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getTotalQuantitySold() {
        return totalQuantitySold;
    }

    public void addSoldQuantity(int quantity) {
        this.totalQuantitySold += quantity;
    }
}
