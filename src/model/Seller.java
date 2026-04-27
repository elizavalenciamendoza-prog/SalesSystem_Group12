package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a seller and all the sales associated with that seller.
 */
public class Seller {

    private final String documentType;
    private final long id;
    private final String firstName;
    private final String lastName;
    private final List<Sale> sales;

    public Seller(String documentType, long id, String firstName, String lastName) {
        this.documentType = documentType;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sales = new ArrayList<Sale>();
    }

    public String getDocumentType() {
        return documentType;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void addSale(Sale sale) {
        sales.add(sale);
        sale.getProduct().addSoldQuantity(sale.getQuantity());
    }

    public double getTotalSalesAmount() {
        double total = 0.0;
        for (Sale sale : sales) {
            total += sale.getTotalAmount();
        }
        return total;
    }
}
