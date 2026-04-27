package model;

/**
 * Represents a sale line associated with one product and quantity.
 */
public class Sale {

    private final Product product;
    private final int quantity;

    public Sale(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalAmount() {
        return product.getPrice() * quantity;
    }
}
