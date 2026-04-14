package model;

import java.util.List; 

public class Sale {
    private Product product;
    private int quantity;

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

    public void printInfo() {
        System.out.println("Producto: " + product.getProductName() + " | Cantidad: " + quantity + " | Precio Unitario: $" + product.getPrice() + " | Total: $" + (product.getPrice() * quantity));
    }
}