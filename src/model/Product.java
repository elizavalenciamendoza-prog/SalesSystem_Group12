package model;

/**
 * Represents a product available for sale.
 */
public class Product {
    private String id;
    private String productName;
    private double price;

    public Product(String id, String productName, double price) {
        this.id = id;
        this.productName = productName;
        this.price = price;
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
}
